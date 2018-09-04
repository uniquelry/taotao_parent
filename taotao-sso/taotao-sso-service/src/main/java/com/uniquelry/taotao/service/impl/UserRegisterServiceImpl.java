package com.uniquelry.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.uniquelry.taotao.mapper.TbUserMapper;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbUser;
import com.uniquelry.taotao.pojo.TbUserExample;
import com.uniquelry.taotao.pojo.TbUserExample.Criteria;
import com.uniquelry.taotao.service.UserRegisterService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月3日 上午9:42:52
 * @Description 
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {
	@Autowired
	private TbUserMapper mapper;
	
	@Override
	public TaotaoResult checkData(String param, Integer type) {
		//1.注入mapper
		//2.根据参数动态的生成查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if(type==1) {//username
			if(StringUtils.isEmpty(param)) {
				return TaotaoResult.ok(false);
			}
			criteria.andUsernameEqualTo(param);
		}else if(type==2) {//phone
			criteria.andPhoneEqualTo(param);
		}else if(type==3) {//email
			criteria.andEmailEqualTo(param);
		}else {//非法参数
			return TaotaoResult.build(400, "非法的参数");
		}
		//3.调用mapper的查询方法，获取数据
		List<TbUser> list = mapper.selectByExample(example);
		//4.如果查询到了数据--数据不可用
		if(list!=null&&list.size()>0) {
			return TaotaoResult.ok(false);
		}
		//5.如果没有查询到--数据可用
		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser user) {
		//1.注入mapper
		//2.校验数据
			//2.1校验用户名和密码不能为空
		if(StringUtils.isEmpty(user.getUsername())) {
			return TaotaoResult.build(400, "注册失败，用户名不能为空，请校验数据后再提交数据");
		}
		if(StringUtils.isEmpty(user.getPassword())) {
			return TaotaoResult.build(400, "注册失败，密码不能为空，请校验数据后再提交数据");
		}
			//2.2校验用户名是否被注册了
		TaotaoResult result1=checkData(user.getUsername(), 1);
		if(!(boolean) result1.getData()) {
			//用户名不可用
			return TaotaoResult.build(400, "注册失败，用户名被占用，请校验数据后再提交数据");
		}
			//2.3校验手机号是否被注册了
		if(StringUtils.isNotBlank(user.getPhone())) {
			TaotaoResult result2=checkData(user.getPhone(), 2);
			if(!(boolean) result2.getData()) {
				//手机号不可用
				return TaotaoResult.build(400, "注册失败，手机号被占用，请校验数据后再提交数据");
			}
		}
			//2.4校验邮箱是否被注册了
		if(StringUtils.isNotBlank(user.getEmail())) {
			TaotaoResult result3=checkData(user.getEmail(), 3);
			if(!(boolean) result3.getData()) {
				//邮箱名不可用
				return TaotaoResult.build(400, "注册失败，邮箱名被占用，请校验数据后再提交数据");
			}
		}
		
		//3.如果校验成功，补全其他属性
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//4.对密码进行MD5加密
		String md5password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5password);
		//5.插入数据
		mapper.insertSelective(user);
		//6.返回taotaoResult
		return TaotaoResult.ok();
	}

}
