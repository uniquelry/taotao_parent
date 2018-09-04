package com.uniquelry.taotao.service;

import com.uniquelry.taotao.pojo.TaotaoResult;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月3日 下午3:53:37
 * @Description 用户登录的接口
 */
public interface UserLoginService {
	/**
	 * 根据用户名和密码登录
	 * @param username
	 * @param password
	 * @return taotaoResult 登录成功，返回200，并包含一个token数据；登录失败，返回400
	 */
	public TaotaoResult login(String username,String password);
	/**
	 * 根据token获取用户信息
	 * @param token
	 * @return 应该包含用户信息
	 */
	public TaotaoResult getUserByToken(String token);
}
