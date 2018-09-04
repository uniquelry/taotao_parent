package com.uniquelry.taotao.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月24日 上午10:38:57
 * @Description 
 */
@Controller
public class HtmlGen {
	@Autowired
	private FreeMarkerConfig config;
	
	@RequestMapping("genHtml")
	@ResponseBody
	public String gen() throws Exception{
		//生成静态页面
		//1.根据config，获取configuration对象
		Configuration configuration = config.getConfiguration();
		//2.设置模板文件，加载模板文件 /WEB-INF/ftl/相对路径
		Template template = configuration.getTemplate("template.ftl");
		//3.创建数据集--》从数据库中获取数据
		Map model= new HashMap<>();
		model.put("springtestkey", "hello");
		//4.创建writer
		Writer writer = new FileWriter(new File("E:/freemarker/springtestfreemarker.html"));
		//5.调用方法输出
		template.process(model, writer);
		//6.关闭流
		writer.close();
		return "ok";
	}
}
