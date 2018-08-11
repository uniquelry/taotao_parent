package com.uniquelry.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbContent;
import com.uniquelry.taotao.service.ContentService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月8日 下午8:20:15
 * @Description 内容表相关的controller
 */
@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="content/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveContent(TbContent content) {
		return contentService.saveContent(content);
	}
}
