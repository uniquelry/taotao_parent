package com.uniquelry.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.mapper.TbOrderItemMapper;
import com.uniquelry.taotao.mapper.TbOrderMapper;
import com.uniquelry.taotao.mapper.TbOrderShippingMapper;
import com.uniquelry.taotao.order.jedis.JedisClient;
import com.uniquelry.taotao.pojo.OrderInfo;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbOrderItem;
import com.uniquelry.taotao.pojo.TbOrderShipping;
import com.uniquelry.taotao.service.OrderService;

/**
 * @Author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月4日 下午11:59:57
 * @Description 
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private JedisClient client;
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Value("${GEN_ORDER_ID_KEY}")
	private String GEN_ORDER_ID_KEY;
	
	@Value("${GEN_ORDER_ID_INIT}")
	private String GEN_ORDER_ID_INIT;
	
	@Value("${GEN_ORDER_ITEM_ID_KEY}")
	private String GEN_ORDER_ITEM_ID_KEY;

	@Override
	public TaotaoResult createOrder(OrderInfo info) {
		//1.插入订单表
			//通过redis的incr，生成订单的id
		//判断如果key不存在，需要初始化key，设置初始值
		if(!client.exists(GEN_ORDER_ID_KEY)) {
			client.set(GEN_ORDER_ID_KEY, GEN_ORDER_ID_INIT);
		}
		 String orderId = client.incr(GEN_ORDER_ID_KEY).toString();
		 info.setOrderId(orderId);
			//补全其他属性
		 //info.setBuyerNick(buyerNick);//在controller里面设置
		 info.setCreateTime(new Date());
		 info.setPostFee("0");
		 info.setStatus(1);
		 //info.setUserId(userId);//在controller里面设置
		 info.setUpdateTime(info.getCreateTime());
		 //插入订单
		 orderMapper.insert(info);
		 
		//2.插入订单项表
		List<TbOrderItem> orderItems = info.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//设置订单项的id，通过redis的incr生成订单项的id
			String orderItemId = client.incr(GEN_ORDER_ITEM_ID_KEY).toString();
			tbOrderItem.setId(orderItemId);
			//补全其他属性
			tbOrderItem.setOrderId(orderId);
			//插入订单项
			orderItemMapper.insert(tbOrderItem);
		}
		 
		//3.插入订单物流表
		TbOrderShipping shipping = info.getOrderShipping();
			//设置订单id
		shipping.setOrderId(orderId);
			//补全其他属性
		shipping.setCreated(info.getCreateTime());
		shipping.setUpdated(info.getUpdateTime());
		//插入订单物流
		orderShippingMapper.insert(shipping);
		
		//返回需要包含订单id的taotaoResult
		return TaotaoResult.ok(orderId);
	}

}
