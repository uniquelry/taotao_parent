package com.uniquelry.taotao.service;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbUser;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月3日 上午9:39:10
 * @Description 用户注册的接口
 */
public interface UserRegisterService {
	/**
	 * 根据参数和类型来校验数据
	 * @param param 要校验的数据
	 * @param type 1 2 3 分别代表username，phone，email
	 * @return
	 */
	public TaotaoResult checkData(String param,Integer type);
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public TaotaoResult register(TbUser user);
}
