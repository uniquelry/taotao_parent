package com.uniquelry.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.cart.jedis.JedisClient;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.service.CartService;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @Author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年9月4日 下午11:45:26
 * @Description 
 */
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private JedisClient client;
	@Value("${TT_CART_REDIS_KEY}")
	private String TT_CART_REDIS_KEY;
	
	@Override
	public TaotaoResult addCartList(Long userId, TbItem tbItem, Integer num) {
		//1.查询
		TbItem itemRedis =queryItemByItemIdAndUserId(tbItem.getId(),userId);
		//2.判断要添加的商品是否存在于Redis购物车列表中
		if(itemRedis!=null) {
			//3.如果存在，直接数量相加
			itemRedis.setNum(itemRedis.getNum()+num);
			//设置到Redis中
			client.hset(TT_CART_REDIS_KEY+":"+userId, itemRedis.getId()+"", JsonUtils.objectToJson(itemRedis));
		}else {
			//4.如果不存在，直接添加到Redis中
			//查询商品的数据（商品的名称，商品的价格，商品的图片），可以调用商品的服务或者直接从controller中传递过来
			//设置商品数量
			tbItem.setNum(num);
			//设置商品中的图片为一张
			if(tbItem.getImage()!=null) {
				tbItem.setImage(tbItem.getImage().split(",")[0]);
			}
			//设置到Redis中
			client.hset(TT_CART_REDIS_KEY+":"+userId, tbItem.getId()+"", JsonUtils.objectToJson(tbItem));
		}
		return TaotaoResult.ok();
	}

	@Override
	public List<TbItem> getCartList(Long userId) {
		Map<String, String> map = client.hgetAll(TT_CART_REDIS_KEY+":"+userId);
		List<TbItem> list=new ArrayList<>();
		if(map!=null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String value = entry.getValue();	//商品json数据
				//转成pojo
				TbItem item=JsonUtils.jsonToPojo(value, TbItem.class);
				list.add(item);
			}
		}
		return list;
	}

	@Override
	public TaotaoResult updateCartListByItemId(Long userId, Long itemId, Integer num) {
		//根据用户id和商品id获取商品对象
		TbItem tbItem = queryItemByItemIdAndUserId(itemId, userId);
		//判断是否存在
		if(tbItem!=null) {
			//更新数量
			tbItem.setNum(num);
			//设置回Redis中
			client.hset(TT_CART_REDIS_KEY+":"+userId, itemId+"", JsonUtils.objectToJson(tbItem));
		}
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteCartListByItemId(Long userId, Long itemId) {
		client.hdel(TT_CART_REDIS_KEY+":"+userId, itemId+"");
		return TaotaoResult.ok();
	}
	
	/**
	 * 通过itemId和userId在Redis购物车中查找商品
	 * @param itemId
	 * @param userId
	 * @return
	 */
	private TbItem queryItemByItemIdAndUserId(Long itemId, Long userId) {
		String string = client.hget(TT_CART_REDIS_KEY+":"+userId, itemId+"");
		if(StringUtils.isNoneBlank(string)) {
			TbItem tbItem=JsonUtils.jsonToPojo(string, TbItem.class);
			return tbItem;
		}
		return null;
	}
	
}
