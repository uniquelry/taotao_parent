package com.uniquelry.taotao.service;

import java.util.List;

import com.uniquelry.taotao.pojo.EasyUITreeNode;
import com.uniquelry.taotao.pojo.TaotaoResult;

/**
 * @author uniquelry
 * @Date 2018年8月8日 上午10:55:50
 * @Description 内容分类业务层的接口
 */
public interface ContentCategoryService {
	//通过节点id查询该节点的子节点列表
	List<EasyUITreeNode> getContentCategoryList(Long parentId);
	//添加内容分类
	TaotaoResult createContentCategory(Long parentId,String name);
	//根据id修改内容分类
	void updateContentCategory(Long id,String name);
}
