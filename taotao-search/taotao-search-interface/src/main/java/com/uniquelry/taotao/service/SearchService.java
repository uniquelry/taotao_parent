package com.uniquelry.taotao.service;

import com.uniquelry.taotao.pojo.SearchResult;
import com.uniquelry.taotao.pojo.TaotaoResult;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月14日 下午2:37:24
 * @Description 
 */
public interface SearchService {
	//导入所有的商品数据到索引库中
	public TaotaoResult importAllSearchItems() throws Exception;
	/**
	 * 根据搜索条件查询结果
	 * @param queryString	查询的主条件
	 * @param page	查询当前的页码
	 * @param rows	每页显示的行数，这个可以在controller中写死
	 * @return
	 * @throws Exception
	 */
	public SearchResult search(String queryString,Integer page,Integer rows) throws Exception;
	
}
