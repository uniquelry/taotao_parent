package com.uniquelry.taotao.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.service.UserLoginService;
import com.uniquelry.taotao.utils.CookieUtils;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月3日 下午4:34:58
 * @Description 
 */
@Controller
public class UserLoginController {
	@Autowired
	private UserLoginService userLoginService;
	
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	/**
	 * 请求url：/user/login
	 * 请求参数：username，password
	 * 返回值：json
	 * 请求限定的方法：post
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(HttpServletRequest request,HttpServletResponse response,String username,String password) {
		//1.引入服务
		//2.注入服务
		//3.调用服务
		TaotaoResult result = userLoginService.login(username, password);
		//4.需要设置token到cookie中，可以使用工具类，cookie需要设置跨域
		if(result.getStatus()==200) {
			CookieUtils.setCookie(request, response, TT_TOKEN_KEY, result.getData().toString());
		}
		return result;
	}
	
	/**
	 * url：/user/token/{token}
	 * 参数：token
	 * 返回值：json
	 * 请求限定的方法：get
	 * @param token
	 * @return
	 */
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback) {
		//1.调用服务
		TaotaoResult result = userLoginService.getUserByToken(token);
		//判断是否时jsonp请求
		if(StringUtils.isNotBlank(callback)) {
			//如果是jsonp，需要拼接类似于fun({id:1})
			String jsonstr=callback+"("+JsonUtils.objectToJson(result)+")";
			return jsonstr;
		}
		//如果不是jsonp
		return JsonUtils.objectToJson(result);
	}
	
}
