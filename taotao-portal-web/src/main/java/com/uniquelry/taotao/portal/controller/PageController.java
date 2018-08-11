package com.uniquelry.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniquelry.taotao.pojo.TbContent;
import com.uniquelry.taotao.portal.pojo.Ad1Node;
import com.uniquelry.taotao.service.ContentService;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @author uniquelry
 * @Date 2018年8月7日 下午10:47:33
 * @Description 展示首页
 */
@Controller
public class PageController {
	
	@Autowired
	private ContentService contentSerivce;
	
	@Value("${AD1_CATEGORY_ID}")
	private Long AD1_CATEGORY_ID;
	
	@Value("${AD1_HEIGHT}")
	private String AD1_HEIGHT;
	
	@Value("${AD1_HEIGHT_B}")
	private String AD1_HEIGHT_B;
	
	@Value("${AD1_WIDTH}")
	private String AD1_WIDTH;
	
	@Value("${AD1_WIDTH_B}")
	private String AD1_WIDTH_B;
	
	@RequestMapping("index")
	public String showIndex(Model model) {
		//添加业务逻辑，根据内容分类的id查询内容列表
		List<TbContent> list = contentSerivce.getContentListByCatId(AD1_CATEGORY_ID);
		//转成自定义的pojo列表
		List<Ad1Node> nodes=new ArrayList<>();
		for (TbContent tbContent : list) {
			Ad1Node node=new Ad1Node();
			node.setAlt(tbContent.getSubTitle());
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setHref(tbContent.getUrl());
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			nodes.add(node);
		}
		//传递数据给jsp
		model.addAttribute("ad1", JsonUtils.objectToJson(nodes));
		return "index";
	}
}
