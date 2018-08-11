package com.uniquelry.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.jedis.JedisClient;
import com.uniquelry.taotao.mapper.TbContentMapper;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbContent;
import com.uniquelry.taotao.pojo.TbContentExample;
import com.uniquelry.taotao.service.ContentService;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月8日 下午8:12:43
 * @Description 
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;
	
	@Override
	public TaotaoResult saveContent(TbContent content) {
		//补全其他的属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入内容表中
		contentMapper.insertSelective(content);
		
		//当添加内容时，需要清空此内容所属分类下的所有缓存
		try {
			jedisClient.hdel(CONTENT_KEY, content.getCategoryId()+"");
			System.out.println("当插入时，清空缓存！！！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentListByCatId(Long categoryId) {
		
		//判断redis中是否存在该数据，如果有直接从redis中获取后返回
		try {
			String jsonstr = jedisClient.hget(CONTENT_KEY, categoryId+"");	//从redis数据库中获取数据
			//如果在，则说明有缓存
			if(StringUtils.isNoneBlank(jsonstr)) {
				System.out.println("redis中已存在该数据缓存！！！");
				return JsonUtils.jsonToList(jsonstr, TbContent.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果redis中没有该数据，先调用dao去数据库中查找，再返回
		//创建example
		TbContentExample example = new TbContentExample();
		//设置查询条件
		example.createCriteria().andCategoryIdEqualTo(categoryId);	//select * from tbcontent where category_id=1;
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);

		//将数据写入到redis缓存中，添加缓存不能影响正常的业务逻辑
		try {
			System.out.println("没有缓存！！！");
			//调用方法写入到redis中
			jedisClient.hset(CONTENT_KEY, categoryId+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//返回结果
		return list;
	}

}
