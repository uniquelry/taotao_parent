package com.uniquelry.taotao.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.mapper.TbContentMapper;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbContent;
import com.uniquelry.taotao.service.ContentService;

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
	
	@Override
	public TaotaoResult saveContent(TbContent content) {
		//补全其他的属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入内容表中
		contentMapper.insertSelective(content);
		return TaotaoResult.ok();
	}

}
