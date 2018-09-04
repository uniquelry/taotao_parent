package com.uniquelry.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniquelry.taotao.pojo.Item;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbItemDesc;
import com.uniquelry.taotao.service.ItemService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月19日 下午7:30:02
 * @Description 
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value="/item/{itemId}")
	public String getItem(@PathVariable Long itemId,Model  model) {
		//调用service的方法
			//商品的基本信息
		TbItem tbItem = itemService.getItemById(itemId);
			//商品的详细描述
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		//转成item
		Item item = new Item(tbItem);
		//将数据传到页面中 
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}
}
