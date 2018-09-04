package com.uniquelry.taotao.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.uniquelry.taotao.mapper.TbUserMapper;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbUser;
import com.uniquelry.taotao.pojo.TbUserExample;
import com.uniquelry.taotao.pojo.TbUserExample.Criteria;
import com.uniquelry.taotao.service.UserLoginService;
import com.uniquelry.taotao.sso.jedis.JedisClient;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月3日 下午3:58:55
 * @Description 
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient client;
	
	@Value("${USER_INFO}")
	private String USER_INFO;
	
	@Value("${EXPIRE_TIME}")
	private Integer EXPIRE_TIME;
	
	@Override
	public TaotaoResult login(String username, String password) {
		//1.注入mapper
		//2.校验用户名和密码是否为空
		if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
			return TaotaoResult.build(400, "用户名和密码不能为空");
		}
		//3.先校验用户名
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list==null||list.size()==0) {
			return TaotaoResult.build(400, "该用户不存在");
		}
		//4.再校验密码
		TbUser user = list.get(0);
		//先加密 密码，再比较
		String md5password = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!md5password.equals(user.getPassword())) {	//表示密码不正确
			return TaotaoResult.build(400, "用户名密码不匹配");
		}
		//5.如果校验成功
		//6.生成token：uuid生成，还需要设置token的有效期来模拟session，用户的数据存放在Redis（key:token,value:用户数据json）
		String token = UUID.randomUUID().toString();
		//存放数据到Redis中，使用jedis的客户端中，为了方便管理加一个前缀"kkk:token"
		//设置密码为空
		user.setPassword(null);
		client.set(USER_INFO+":"+token, JsonUtils.objectToJson(user));
		//设置过期时间，来模拟session
		client.expire(USER_INFO+":"+token, EXPIRE_TIME);
		//7.把token设置再cookie中，在表现层中设置
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		//1.注入jedisclient
		//2.调用根据token查询，用户信息的方法，get方法
		String strjson = client.get(USER_INFO+":"+token);
		//3.判断是否查询得到
		
		if(StringUtils.isNotBlank(strjson)) {
			//如果查询到，需要返回200，包含用户信息，转成对象
			TbUser user = JsonUtils.jsonToPojo(strjson, TbUser.class);
			//重新设置Redis过期时间
			client.expire(USER_INFO+":"+token,EXPIRE_TIME);
			return TaotaoResult.ok(user);
		}
		//如果查询不到，返回400
		return TaotaoResult.build(400, "用户已过期");
	}

}
