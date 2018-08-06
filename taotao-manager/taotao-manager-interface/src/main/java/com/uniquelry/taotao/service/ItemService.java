package com.uniquelry.taotao.service;
/**
 * @author uniquelry
 * @Date 2018年8月5日 下午5:52:43
 * @Description 商品相关处理的service接口
 */

import com.uniquelry.taotao.pojo.EasyUIDataGridResult;

public interface ItemService {
	/**
	 * 根据当前页码和每页的行数进行分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	public EasyUIDataGridResult getItemList(Integer page,Integer rows);
}
