package com.uniquelry.taotao.service;

import com.uniquelry.taotao.pojo.OrderInfo;
import com.uniquelry.taotao.pojo.TaotaoResult;

/**
 * @Author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月4日 下午11:49:32
 * @Description 
 */
public interface OrderService {
	/**
	 * 创建订单
	 * @param info
	 * @return
	 */
	public TaotaoResult createOrder(OrderInfo info);
}
