package com.uniquelry.taotao.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uniquelry.taotao.mapper.TbItemMapper;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbItemExample;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午4:28:57
 * @Description 
 */
public class TestPageHelper {
	
	@Test
	public void testHelper() {
		//1.初始化spring容器
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//2.获取mapper的代理对象
		TbItemMapper itemMapper = context.getBean(TbItemMapper.class);
		//3.设置分页信息
		PageHelper.startPage(1, 5);//3行 紧跟着的第一个查询才会被分页
		//4.调用mapper的方法查询数据
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);//select * from tb_item;
		List<TbItem> list2 = itemMapper.selectByExample(example);//select * from tb_item;
		//5.取分页信息
		PageInfo<TbItem> info = new PageInfo<>(list);
		//6.遍历结果集，打印效果
		System.out.println("第一个查询的list集合长度："+list.size());
		System.out.println("第二个查询的list集合长度："+list2.size());
		System.out.println("查询的总记录数："+info.getTotal());
		for (TbItem tbItem : list) {
			System.out.println(tbItem.getId()+":"+tbItem.getTitle()+":"+tbItem.getPrice());
		}
		
	}
}
