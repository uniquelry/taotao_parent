package com.uniquelry.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月11日 下午10:24:36
 * @Description 测试Jedis
 */
public class JedisTest {
	//测试单机版
	@Test
	public void testJedis() {
		//1.创建jedis对象，需要指定端口和地址
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		//2.直接操作redis set
		jedis.set("key1234", "value1234");
		System.out.println(jedis.get("key1234"));
		//3.关闭jedis
		jedis.close();
	}
	
	//单机版JedisPool
	@Test
	public void testJedisPool() {
		//1.创建jedisPool对象，需要端口号和地址
		JedisPool jedisPool = new JedisPool("192.168.25.128", 6379);
		//2.获取jedis对象
		Jedis jedis = jedisPool.getResource();
		//3.直接操作redis
		jedis.set("keypool", "valuepool");
		System.out.println(jedis.get("keypool"));
		//4.关闭jedis(释放资源到连接池)
		jedis.close();
		//5.关闭连接池(一般在应用系统关闭的时候才关闭)
		jedisPool.close();
	}
	
	//测试集群版
	@Test
	public void testJedisCluster() {
		Set<HostAndPort> nodes=new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));
		//1.创建jedisCluster对象
		JedisCluster cluster = new JedisCluster(nodes);
		//2.直接根据jedisCluster对象操作redis集群
		cluster.set("keyCluster", "valueCluster");
		System.out.println(cluster.get("keyCluster"));
		//3.关闭jedisCluster对象(一般在应用系统关闭时才关闭)封装了连接池
		cluster.close();
	}
}
