package com.uniquelry.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.service.SearchService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月14日 下午3:34:54
 * @Description 
 */
@Controller
public class ImportAllItems {
	
	@Autowired
	private SearchService searchService;
	
	/**
	 * 导入所有的商品中的数据到索引库中
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/index/importAllSearch")
	@ResponseBody
	public TaotaoResult importAllSearch() throws Exception {
		return searchService.importAllSearchItems();
	}
}
