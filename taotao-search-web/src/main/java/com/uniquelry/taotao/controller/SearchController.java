package com.uniquelry.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniquelry.taotao.pojo.SearchResult;
import com.uniquelry.taotao.service.SearchService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月14日 下午11:05:07
 * @Description 
 */
@Controller
public class SearchController {
	@Value("${ITEM_ROWS}")
	private Integer ITEM_ROWS;
	
	@Autowired
	private SearchService searchService;
	
	/**
	 * 根据条件搜索商品数据
	 * @param page
	 * @param queryString
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/search")
	public String search(@RequestParam(defaultValue="1") Integer page,@RequestParam(value="q") String queryString,Model model) throws Exception {
		//处理get请求乱码问题
		queryString = new String(queryString.getBytes("iso-8859-1"),"utf-8");
		
		SearchResult searchResult = searchService.search(queryString, page, ITEM_ROWS);
		
		//设置响应数据到jsp中
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);
		return "search";
	}
	
}
