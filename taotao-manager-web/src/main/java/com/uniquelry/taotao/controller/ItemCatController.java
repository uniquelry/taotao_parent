package com.uniquelry.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.EasyUITreeNode;
import com.uniquelry.taotao.service.ItemCatService;

/**
 * @author uniquelry
 * @Date 2018年8月6日 下午9:58:53
 * @Description 商品分类管理的Contorller
 */
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0") Long parentId){
		return itemCatService.getItemCatList(parentId);
	}
	
}
