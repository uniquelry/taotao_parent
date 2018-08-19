package com.uniquelry.taotao.service;

import com.uniquelry.taotao.pojo.EasyUIDataGridResult;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbItemDesc;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午5:52:43
 * @Description 商品相关处理的service接口
 */
public interface ItemService {
	/**
	 * 根据当前页码和每页的行数进行分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGridResult getItemList(Integer page,Integer rows);
	/**
	 * 添加商品基本数据和描述数据
	 * @param item
	 * @param desc
	 * @return
	 */
	TaotaoResult addItem(TbItem item,String desc);
	/**
	 * 根据商品的id查询商品数据
	 * @param itemId
	 * @return
	 */
	public TbItem getItemById(Long itemId);
	/**
	 * 根据商品的id查询商品详情
	 * @param itemId
	 * @return
	 */
	public TbItemDesc getItemDescById(Long itemId);
}
