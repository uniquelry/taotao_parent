package com.uniquelry.taotao.service;

import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbContent;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月8日 下午8:07:33
 * @Description 内容业务层的接口
 */
public interface ContentService {
	/**
	 * 插入内容
	 * @param content
	 * @return
	 */
	TaotaoResult saveContent(TbContent content);
}
