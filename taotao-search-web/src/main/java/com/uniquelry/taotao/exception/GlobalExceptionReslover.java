package com.uniquelry.taotao.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月16日 下午11:59:40
 * @Description 全局异常处理器类
 */
public class GlobalExceptionReslover implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object hanlder,
			Exception ex) {
		//1.日志写入到日志文件，打印
		System.out.println(ex.getMessage());
		//2.及时通知开发人员，发短信，发邮件（通过第三方接口）
		System.out.println("发短信给开发人员。。。");
		//3.给用户一个友好的提示：你的网络有异常，请重试
		ModelAndView modelAndView = new ModelAndView();
		//设置视图信息
		modelAndView.setViewName("error/exception");
		//设置模型数据
		modelAndView.addObject("message","您的网络有异常，请稍后重试");
		return modelAndView;
	}

}
