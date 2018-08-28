package com.uniquelry.taotao.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.uniquelry.taotao.pojo.Item;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbItemDesc;
import com.uniquelry.taotao.service.ItemService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月24日 下午4:32:46
 * @Description 监听器，获取消息，执行生成静态页面的业务逻辑
 */
public class ItemChangeGenHtmlMessageListener implements MessageListener {
	@Autowired
	private ItemService itemService;
	@Autowired 
	private FreeMarkerConfig config;

	@Override
	public void onMessage(Message message) {
		if(message instanceof TextMessage) {
			//1.获取消息，商品的id
			TextMessage textMessage=(TextMessage) message;
			try {
				Long itemId=Long.valueOf(textMessage.getText());
				//2.从数据库中获取数据，可以调用manager中的服务，获取到了数据集
				TbItem tbItem = itemService.getItemById(itemId);
				Item item = new Item(tbItem);
				TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
				//3.生成静态页面，准备好模板和数据集
				genHtmlFreemarker(item,tbItemDesc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成静态页面
	 * @param item
	 * @param tbItemDesc
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws MalformedTemplateNameException 
	 * @throws TemplateNotFoundException 
	 */
	private void genHtmlFreemarker(Item item, TbItemDesc tbItemDesc) throws Exception {
		//1.获取configuration对象
		Configuration configuration = config.getConfiguration();
		//2.创建模板，获取模板文件对象
		Template template = configuration.getTemplate("item.ftl");
		//3.创建数据集
		Map model=new HashMap<>();
		model.put("item", item);
		model.put("itemDesc", tbItemDesc);
		//4.输出
		Writer writer = new FileWriter(new File("E:\\freemarker\\item"+"\\"+item.getId()+".html"));
		template.process(model, writer);
		//5.关闭流
		writer.close();
	}

}
