package com.uniquelry.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uniquelry.taotao.manager.jedis.JedisClient;
import com.uniquelry.taotao.mapper.TbItemDescMapper;
import com.uniquelry.taotao.mapper.TbItemMapper;
import com.uniquelry.taotao.pojo.EasyUIDataGridResult;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbItemDesc;
import com.uniquelry.taotao.pojo.TbItemExample;
import com.uniquelry.taotao.service.ItemService;
import com.uniquelry.taotao.utils.IDUtils;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午5:57:07
 * @Description item的业务层的实现类
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapperr;
	
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	
	@Resource(name="topicDestination")
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination destination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ITEM_INFO_KEY}")
	private String ITEM_INFO_KEY;
	
	@Value("${ITEM_INFO_KEY_EXPIRE}")
	private Integer ITEM_INFO_KEY_EXPIRE;
	
	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//设置分页的信息，使用pagehelper
		if(page==null) {
			page=1;
		}
		if(rows==null) {
			rows=30;
		}
		PageHelper.startPage(page, rows);
		//创建example对象，不需要设置查询条件
		TbItemExample example=new TbItemExample();
		//根据mapper调用查询所有数据的方法
		List<TbItem> list = tbItemMapperr.selectByExample(example);
		//获取分页信息
		PageInfo<TbItem> info=new PageInfo<>(list);
		//封装到EasyUIDataGridResult
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) info.getTotal());
		result.setRows(info.getList());
		//返回
		return result;
	}

	@Override
	public TaotaoResult addItem(TbItem item, String desc) {
		//生成商品id
		long itemId = IDUtils.genItemId();
		//补全item属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表中插入数据
		tbItemMapperr.insert(item);
		//创建商品描述表对应的pojo
		TbItemDesc tbItemDesc = new TbItemDesc();
		//补全pojo的属性
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		//向商品描述表中插入数据
		tbItemDescMapper.insert(tbItemDesc);
		
		//添加发送消息业务逻辑
		jmsTemplate.send(destination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				//发送的消息内容
				return session.createTextMessage(itemId+"");
			}
		});
		
		//返回结果
		return TaotaoResult.ok();
	}

	@Override
	public TbItem getItemById(Long itemId) {
		//1.从缓存中获取数据，如果有直接返回
		try {
			String jsonstr = jedisClient.get(ITEM_INFO_KEY+":"+itemId+":BASE");
			if(StringUtils.isNoneBlank(jsonstr)) {
				System.out.println("redis中该商品有base缓存...");
				//重新设置商品的有效期
				jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":BASE", ITEM_INFO_KEY_EXPIRE);
				return JsonUtils.jsonToPojo(jsonstr, TbItem.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//2.如果没有数据，从数据库中获取
		System.out.println("redis中该商品没有base缓存...");
		TbItem tbItem = tbItemMapperr.selectByPrimaryKey(itemId);
		
		try {
			//3.添加缓存到redis数据库中
			jedisClient.set(ITEM_INFO_KEY+":"+itemId+":BASE", JsonUtils.objectToJson(tbItem));
			//4.设置缓存的有效期
			jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":BASE", ITEM_INFO_KEY_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tbItem;
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {
		//1.从缓存中获取数据，如果有直接返回
		try {
			String jsonstr = jedisClient.get(ITEM_INFO_KEY+":"+itemId+":DESC");
			if(StringUtils.isNoneBlank(jsonstr)) {
				System.out.println("redis中该商品有desc缓存...");
				//重新设置商品的有效期
				jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":DESC", ITEM_INFO_KEY_EXPIRE);
				return JsonUtils.jsonToPojo(jsonstr, TbItemDesc.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//2.如果没有数据，从数据库中获取
		System.out.println("redis中该商品没有desc缓存...");
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		
		try {
			//3.添加缓存到redis数据库中
			jedisClient.set(ITEM_INFO_KEY+":"+itemId+":DESC", JsonUtils.objectToJson(tbItemDesc));
			//4.设置缓存的有效期
			jedisClient.expire(ITEM_INFO_KEY+":"+itemId+":DESC", ITEM_INFO_KEY_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tbItemDesc;
	}

}
