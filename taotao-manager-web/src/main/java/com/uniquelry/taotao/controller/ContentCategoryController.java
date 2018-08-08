package com.uniquelry.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.EasyUITreeNode;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.service.ContentCategoryService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月8日 下午12:49:22
 * @Description 内容分类的controller
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping(value="content/category/list",method=RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value="id",defaultValue="0") Long parentId){
		return contentCategoryService.getContentCategoryList(parentId);
	}
	
	/**
	 * @param parentId 新增节点的父节点的id
	 * @param name 新增节点的文本
	 * @return TaotaoResult 包含分类的id
	 */
	@RequestMapping(value="content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult createContentCategory(Long parentId,String name) {
		return contentCategoryService.createContentCategory(parentId, name);
	}
	
	@RequestMapping(value="content/category/update",method=RequestMethod.POST)
	public void updateContentCategory(Long id,String name) {
		contentCategoryService.updateContentCategory(id, name);
	}
}
