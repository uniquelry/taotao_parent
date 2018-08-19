package com.uniquelry.taotao.mapper;

import java.util.List;

import com.uniquelry.taotao.pojo.SearchItem;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月14日 下午12:44:03
 * @Description 定义mapper,关联查询3张表，查询出搜索时的商品数据
 */
public interface SearchItemMapper {
	//查询所有的商品数据
	public List<SearchItem> getSearchItemList();
	//根据商品的id查询商品的数据
	public SearchItem getSearchItemById(Long itemId);
}
