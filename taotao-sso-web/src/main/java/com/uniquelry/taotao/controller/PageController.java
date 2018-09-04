package com.uniquelry.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月4日 上午10:05:31
 * @Description 跳转页面使用的controller
 */
@Controller
public class PageController {
	@RequestMapping(value="/page/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
}
