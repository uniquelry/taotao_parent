package com.uniquelry.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbUser;
import com.uniquelry.taotao.service.UserRegisterService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月3日 上午10:53:04
 * @Description 
 */
@Controller
public class UserRegisterController {
	@Autowired
	private UserRegisterService userRegisterService;
	
	/**
	 * 请求url：/user/check/{param}/{type}
	 * 请求method：get
	 * 校验数据
	 * @param param 
	 * @param type 1 2 3 
	 * @return
	 */
	@RequestMapping(value="/user/check/{param}/{type}",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult checkData(@PathVariable String param,@PathVariable Integer type) {
		return userRegisterService.checkData(param, type);
	}
	
	/**
	 * 请求url：/user/register
	 * 请求method：post
	 * 请求参数：
	 * 		username  //用户名
	 * 		password  //密码
	 * 		phone  //手机号
	 * 		email  //邮箱名
	 * 返回值：json
	 * 注册用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user) {
		return userRegisterService.register(user);
	}
}
