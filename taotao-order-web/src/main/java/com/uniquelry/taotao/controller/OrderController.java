package com.uniquelry.taotao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniquelry.taotao.pojo.OrderInfo;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbUser;
import com.uniquelry.taotao.service.CartService;
import com.uniquelry.taotao.service.OrderService;
import com.uniquelry.taotao.service.UserLoginService;
import com.uniquelry.taotao.utils.CookieUtils;

/**
 * @Author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月5日 上午12:09:19
 * @Description 订单处理的controller
 */
@Controller
public class OrderController {
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
		
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/order/order-cart")
	public String showOrder(HttpServletRequest request) {
		/*//1.从cookie中获取用户的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		if(StringUtils.isNotBlank(token)) {
			//2.调用sso服务获取用户信息
			TaotaoResult result = loginService.getUserByToken(token);
			if(result.getStatus()==200) {
				//3.必须是登录了的用户才展示
				//4.展示用户的收货地址，根据用户id查询该用户的配送地址。静态数据
				//5.展示支付方式，从数据库中获取支付的方式。静态数据
				//6.调用购物车的服务从redis数据库中获取购物车的商品列表
				TbUser user=(TbUser) result.getData();
				List<TbItem> cartList = cartService.getCartList(user.getId());
				//7.将列表展示到页面中（传递到页面通过model）
				request.setAttribute("cartList", cartList);
			}
		}*/
		
		TbUser user=(TbUser) request.getAttribute("USER_INFO");
		//调用购物车的服务从redis数据库中获取购物车的商品列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		//7.将列表展示到页面中（传递到页面通过model）
		request.setAttribute("cartList", cartList);
		return "order-cart";
	}
	
	/**
	 * url：/order/create
	 * 参数：表单使用orderInfo来接收
	 * 返回值：逻辑视图
	 * @param request
	 * @param info
	 * @return
	 */
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(HttpServletRequest request,OrderInfo info) {
		//查询到用户的信息，设置到info中
		TbUser user=(TbUser) request.getAttribute("USER_INFO");
		info.setUserId(user.getId());
		info.setBuyerNick(user.getUsername());
		TaotaoResult result = orderService.createOrder(info);
		request.setAttribute("orderId", result.getData());
		request.setAttribute("payment", info.getPayment());
		DateTime dateTime=new DateTime();
		DateTime plusDays = dateTime.plusDays(3);	//加三天
		request.setAttribute("date", plusDays.toString("yyyy-MM-dd"));
		return "success";
	}
}
