package com.uniquelry.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.service.TestService;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午2:03:04
 * @Description 测试使用的Controller，查询当前时间
 */
@Controller
@RequestMapping("test")
public class TestController {
	
	//注入服务
	@Autowired
	private TestService testService;
	
	
	@RequestMapping("queryNow")
	@ResponseBody
	public String queryNow() {
		return testService.queryNow();
	}
}
