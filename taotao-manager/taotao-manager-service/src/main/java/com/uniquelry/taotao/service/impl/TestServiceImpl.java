package com.uniquelry.taotao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.mapper.TestMapper;
import com.uniquelry.taotao.service.TestService;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午1:04:24
 * @Description 测试业务层的实现类
 */
@Service
public class TestServiceImpl implements TestService {
	
	@Autowired
	private TestMapper testMapper;
	
	@Override
	public String queryNow() {
		return testMapper.queryNow();
	}

}
