package com.uniquelry.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author uniquelry
 * @Date 2018年8月7日 下午10:47:33
 * @Description 展示首页
 */
@Controller
public class PageController {
	
	@RequestMapping("index")
	public String showIndex() {
		return "index";
	}
}
