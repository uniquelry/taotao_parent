package com.uniquelry.taotao.manager.jedis;

/**
 * @author uniquelry
 * @Date 2018年8月11日 下午11:09:24
 * @Description
 */
public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);	
	Long hdel(String key,String... field);//删除hkey
	
}
