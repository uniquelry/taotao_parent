package com.uniquelry.taotao.controller;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.EasyUIDataGridResult;
import com.uniquelry.taotao.service.ItemService;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午8:45:51
 * @Description 
 */
@Controller
@RequestMapping("item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
		return itemService.getItemList(page, rows);
	}
}
