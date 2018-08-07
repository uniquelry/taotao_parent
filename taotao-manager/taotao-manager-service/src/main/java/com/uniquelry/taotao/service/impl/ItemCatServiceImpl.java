package com.uniquelry.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.mapper.TbItemCatMapper;
import com.uniquelry.taotao.pojo.EasyUITreeNode;
import com.uniquelry.taotao.pojo.TbItemCat;
import com.uniquelry.taotao.pojo.TbItemCatExample;
import com.uniquelry.taotao.pojo.TbItemCatExample.Criteria;
import com.uniquelry.taotao.service.ItemCatService;

/**
 * @author uniquelry
 * @Date 2018年8月6日 下午9:39:47
 * @Description 商品分类管理业务层的实现类
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		//根据父节点id查询子节点列表
		TbItemCatExample example=new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		//设置parentId
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		//转换成EasyUITreeNode列表
		List<EasyUITreeNode> resultList=new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node=new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			//如果节点有子节点是“closed”，没有子节点是“open”
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到节点列表
			resultList.add(node);
		}
		return resultList;
	}

}
