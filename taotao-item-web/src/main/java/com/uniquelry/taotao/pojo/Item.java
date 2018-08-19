package com.uniquelry.taotao.pojo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月19日 下午6:56:35
 * @Description 
 */
public class Item extends TbItem{
	
	public Item(TbItem tbItem) {
		//将数据拷贝到item中
		BeanUtils.copyProperties(tbItem, this);
	}
	
	public String[] getImages() {
		if(StringUtils.isNoneBlank(super.getImage())) {
			return super.getImage().split(",");					
		}
		return null;
	}
}
