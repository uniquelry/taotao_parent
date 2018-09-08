package com.uniquelry.taotao.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.service.UserLoginService;
import com.uniquelry.taotao.utils.CookieUtils;

/**
 * @Author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月5日 上午12:09:40
 * @Description 用户身份认证拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserLoginService loginService;

	//在进入目标方法之前执行
	//预处理相关工作
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//用户身份在此认证
		//1.取cookie中的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//2.判断token是否存在
		if(StringUtils.isEmpty(token)) {
			//3.如果不存在，说明没登录 ---》重定向到登录页面
			//request.getRequestURL().toString()：就是访问的url localhost:8093/order/order-cart.html
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL().toString());
			return false;
		}
		//4.如果token存在，调用sso的服务，查询用户信息(看看是否过期)
		TaotaoResult result = loginService.getUserByToken(token);
		if(result.getStatus()!=200) {
			//5.如果用户已过期 ---》重定向到登录页面
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURL().toString());
			return false;
		}
		//6.用户没过期（说明登录了）---》放行
		//设置用户信息到request中，目标方法的request就可以获取用户信息
		request.setAttribute("USER_INFO", result.getData());
		return true;
	}

	//在进入目标方法之后，返回ModelAndView之前执行
	//共用变量的一些设置
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	//返回ModelAndView之后，渲染到页面之前执行
	//异常处理，清理工作
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
