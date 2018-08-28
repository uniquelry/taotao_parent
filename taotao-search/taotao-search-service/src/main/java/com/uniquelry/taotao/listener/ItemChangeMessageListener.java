package com.uniquelry.taotao.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.service.SearchService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月19日 下午4:37:51
 * @Description 接收消息的监听器
 */
public class ItemChangeMessageListener implements MessageListener{

	//注入service直接注入
	@Autowired
	private SearchService searchService;
	
	@Override
	public void onMessage(Message message) {
		//判断消息是否为textMessage
		if(message instanceof TextMessage) {
			//如果是，获取商品的id
			TextMessage textMessage=(TextMessage) message;
			String itemidstr;
			try {
				//获取的就是商品id的字符串
				itemidstr = textMessage.getText();
				Long itemId = Long.parseLong(itemidstr);
				//更新索引库
				TaotaoResult result = searchService.updateSearchItem(itemId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  
	}
	
}
