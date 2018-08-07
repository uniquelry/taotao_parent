package com.uniquelry.taotao.service;

import java.util.List;

import com.uniquelry.taotao.pojo.EasyUITreeNode;

/**
 * @author uniquelry
 * @Date 2018年8月6日 下午9:37:50
 * @Description 商品分类管理业务层的接口
 */
public interface ItemCatService {
	List<EasyUITreeNode> getItemCatList(Long parentId);
}
