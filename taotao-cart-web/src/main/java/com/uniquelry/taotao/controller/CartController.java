package com.uniquelry.taotao.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbUser;
import com.uniquelry.taotao.service.CartService;
import com.uniquelry.taotao.service.ItemService;
import com.uniquelry.taotao.service.UserLoginService;
import com.uniquelry.taotao.utils.CookieUtils;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @Author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月4日 下午11:30:03
 * @Description 
 */
@Controller
public class CartController {
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	@Value("${TT_CART_KEY}")
	private String TT_CART_KEY;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserLoginService loginService;
	@Autowired
	private CartService cartService;
	
	/**
	 * 添加购物车
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/cart/add/{itemId}",method=RequestMethod.GET)
	public String addCartItem(@PathVariable Long itemId,Integer num,HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否登录
		//从cookie中获取用户的token信息
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//调用sso的服务查询用户信息
		TaotaoResult result = loginService.getUserByToken(token);
		
		//获取商品的数据
		if(result.getStatus()==200) {
			//如果已经登录，调用cartService服务的方法
			TbUser user=(TbUser) result.getData();
			TbItem tbItem = itemService.getItemById(itemId);
			cartService.addCartList(user.getId(),tbItem,num);
		}else {
			//如果没有登录，调用cookie中的方法，添加到购物车
			addCookieItemCart(request, response, itemId, num);
		}
		return "cartSuccess";
	}
	
	/**
	 * 展示购物车
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,Model model) {
		List<TbItem> cartList =new ArrayList<>();
		//判断用户是否登录
		//从cookie中获取用户的token信息
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//调用sso的服务查询用户信息
		TaotaoResult result = loginService.getUserByToken(token);
		if(result.getStatus()==200) {
			//如果已经登录，调用cartService服务的方法
			TbUser user=(TbUser) result.getData();
			cartList=cartService.getCartList(user.getId());
		}else {
			//如果没有登录，调用cookie的方法，获取购物车列表
			cartList= getCookieCartList(request);
		}
		
		//将cartList数据传递给页面
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 修改购物车商品数量
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping(value="/cart/update/num/{itemId}/{num}",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult updateNum(HttpServletRequest request,HttpServletResponse response,@PathVariable Long itemId,@PathVariable Integer num) {
		//判断用户是否登录
		//从cookie中获取用户的token信息
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//调用sso的服务查询用户信息
		TaotaoResult result = loginService.getUserByToken(token);
		if(result.getStatus()==200) {
			//如果登录成功，调用cartService方法
			TbUser user=(TbUser) result.getData();
			//更新商品的数量
			cartService.updateCartListByItemId(user.getId(),itemId,num);
		}else {
			//如果没有登录，调用cookie方法，更新cookie中的商品数量
			updateCookieItemCart(request, response, itemId, num);
		}
		//响应taotaoResult数据
		return TaotaoResult.ok();
	}
	
	/**
	 * 删除购物车
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否登录
		//从cookie中获取用户的token信息
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//调用sso的服务查询用户信息
		TaotaoResult result = loginService.getUserByToken(token);
		if(result.getStatus()==200) {
			//如果登录成功，调用cartService方法
			TbUser user=(TbUser) result.getData();
			//删除
			cartService.deleteCartListByItemId(user.getId(),itemId);
		}else {
			//如果没有登录，调用cookie中的方法，删除cookie中的商品
			deleteCookieItemCart(request, response, itemId);
		}
		//返回逻辑视图：在逻辑视图中做redirect跳转
		return "redirect:/cart/cart.html";
	}

	//	--------------------------完美分割线----------------------
	
	/**
	 * 从cookie中添加购物车
	 * @param request
	 * @param response
	 * @param itemId
	 */
	private void addCookieItemCart(HttpServletRequest request,HttpServletResponse response,Long itemId,Integer num) {
		//1.从cookie中查询商品列表
		List<TbItem> cartList=getCookieCartList(request);
		//2.判断商品在商品列表中是否存在
		boolean flag=false;
		for (TbItem tbItem : cartList) {
			//对象比较的是地址，此处应该为值的比较
			if(tbItem.getId()==itemId.longValue()) {
				//3.如果存在，商品数量相加
				tbItem.setNum(tbItem.getNum()+num);
				flag=true;
				break;
			}
		}
		if(!flag) {
			//4.不存在，根据商品id查询商品信息
			TbItem tbItem = itemService.getItemById(itemId);
			//取一张图片
			String image = tbItem.getImage();
			if(StringUtils.isNotBlank(image)) {
				String[] images=image.split(",");
				tbItem.setImage(images[0]);
			}
			//设置购买数量
			tbItem.setNum(num);
			//5.把商品添加到购物车
			cartList.add(tbItem);
		}
		//6.把购物车写入到cookie中
		CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cartList),CART_EXPIRE,true);
	}
	
	/**
	 * 获取cookie中的购物车列表
	 * @param request
	 * @return
	 */
	private List<TbItem> getCookieCartList(HttpServletRequest request) {
		//取购物车列表
		String json = CookieUtils.getCookieValue(request, TT_CART_KEY,true);
		//判断json是否为空
		if(StringUtils.isNotBlank(json)) {
			//把json转成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
	
	/**
	 * 更新cookie中购物车中的商品数量
	 * @param request
	 * @param response
	 * @param itemId
	 * @param num
	 */
	private void updateCookieItemCart(HttpServletRequest request,HttpServletResponse response,Long itemId,Integer num) {
		//从cookie中获取商品列表
		List<TbItem> cartList = getCookieCartList(request);
		//遍历商品列表找到对应的商品
		boolean flag=false;
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()) {
				//更新商品数量
				tbItem.setNum(num);
				flag=true;
				break;
			}
		}
		if(flag==true) {
			//如果存在该商品，把商品写入到cookie中
			CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cartList),CART_EXPIRE,true);
		}
	}
	
	/**
	 * 从cookie中删除购物车中的商品
	 * @param request
	 * @param response
	 * @param itemId
	 */
	private void deleteCookieItemCart(HttpServletRequest request,HttpServletResponse response,Long itemId) {
		//从cookie中取购物车商品列表
		List<TbItem> cartList = getCookieCartList(request);
		//遍历列表找到对应的商品
		boolean flag=false;
		for (TbItem tbItem : cartList) {
			if(tbItem.getId()==itemId.longValue()) {
				//删除商品
				cartList.remove(tbItem);
				flag=true;
				break;
			}
		}
		if(flag==true) {
			//把商品列表写入到cookie中
			CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
		}
	}
	
}
