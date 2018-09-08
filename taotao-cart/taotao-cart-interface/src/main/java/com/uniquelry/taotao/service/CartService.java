package com.uniquelry.taotao.service;

import java.util.List;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbItem;

/**
 * @Author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月4日 下午11:37:06
 * @Description 
 */
public interface CartService {
	
	/**
	 * 添加商品到购物车Redis中
	 * @param id
	 * @param tbItem
	 * @param num
	 * @return
	 */
	public TaotaoResult addCartList(Long userId, TbItem tbItem, Integer num);

	/**
	 * 根据用户的ID查询用户的购物车的列表
	 * @param id
	 * @return
	 */
	public List<TbItem> getCartList(Long userId);

	/**
	 * 根据商品的Id更新数量
	 * @param id 用户的id 购物车的id
	 * @param itemId 商品的id
	 * @param num 更新后的数量
	 * @return
	 */
	public TaotaoResult updateCartListByItemId(Long userId, Long itemId, Integer num);

	/**
	 * 根据商品的Id删除商品
	 * @param id
	 * @param itemId
	 * @return
	 */
	public TaotaoResult deleteCartListByItemId(Long userId, Long itemId);

}
