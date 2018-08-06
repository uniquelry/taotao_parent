package com.uniquelry.taotao.controller;
/**
 * @author uniquelry
 * @Date 2018年8月5日 下午3:31:57
 * @Description 显示页面
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageContorller {
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
}
