package com.uniquelry.taotao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uniquelry.taotao.mapper.TbItemMapper;
import com.uniquelry.taotao.pojo.EasyUIDataGridResult;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.pojo.TbItemExample;
import com.uniquelry.taotao.service.ItemService;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午5:57:07
 * @Description item的业务层的实现类
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapperr;
	
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

}
