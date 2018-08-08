package com.uniquelry.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.mapper.TbContentCategoryMapper;
import com.uniquelry.taotao.pojo.EasyUITreeNode;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbContentCategory;
import com.uniquelry.taotao.pojo.TbContentCategoryExample;
import com.uniquelry.taotao.pojo.TbContentCategoryExample.Criteria;
import com.uniquelry.taotao.service.ContentCategoryService;

/**
 * @author uniquelry
 * @Date 2018年8月8日 上午10:59:21
 * @Description 内容分类业务层的实现类
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
		//创建example
		TbContentCategoryExample example = new TbContentCategoryExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);//select * from tbcontentcategory where parent_id=1
		//执行查询
		List<TbContentCategory> list = mapper.selectByExample(example);
		//转成EasyUITreeNode列表
		List<EasyUITreeNode> nodes=new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node=new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			node.setText(tbContentCategory.getName());//分类名称
			nodes.add(node);
		}
		return nodes;
	}

	@Override
	public TaotaoResult createContentCategory(Long parentId, String name) {
		//1.构建对象，补全属性
		TbContentCategory contentCategory=new TbContentCategory();
		contentCategory.setIsParent(false);//新增的节点都是叶子节点
		contentCategory.setCreated(new Date());
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		contentCategory.setUpdated(new Date());
		//2.插入ContentCategory数据
		mapper.insertSelective(contentCategory);
		
		//3.判断父节点本身是否为叶子节点，需要更新其为父节点
		TbContentCategory parent=mapper.selectByPrimaryKey(parentId);
		if(parent.getIsParent()==false) {//原本就是叶子节点
			parent.setIsParent(true);
			//更新父节点的isParent属性为true
			mapper.updateByPrimaryKeySelective(parent);
		}
		
		//4.返回taotaoresult包含内容分类的id
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public void updateContentCategory(Long id, String name) {
		//根据id查询节点
		TbContentCategory contentCategory = mapper.selectByPrimaryKey(id);
		//修改节点的name属性
		contentCategory.setName(name);
		//更新节点
		mapper.updateByPrimaryKeySelective(contentCategory);
	}

}