package com.uniquelry.taotao.jedis;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月11日 下午11:26:15
 * @Description 
 */
public class TestJedisClient {
	
	//测试单机版
	@Test
	public void testPool() {
		//1.初始化spring容器
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		//2.获取实现类实例
		JedisClient bean = context.getBean(JedisClient.class);
		//3.调用方法操作
		bean.set("jedisPoolKey", "jedisPoolValue");
		System.out.println(bean.get("jedisPoolKey"));
	}
	
	//测试集群版
	@Test
	public void testCluster() {
		//1.初始化spring容器
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-jedis.xml");
		//2.获取实现类实例
		JedisClient bean = context.getBean(JedisClient.class);
		//3.调用方法操作
		bean.set("jedisClusterKey", "jedisClusterValue");
		System.out.println(bean.get("jedisClusterKey"));
	}
}
