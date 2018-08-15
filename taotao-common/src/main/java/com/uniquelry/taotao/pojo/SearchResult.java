package com.uniquelry.taotao.pojo;

import java.io.Serializable;
import java.util.List;
/**
 * @author uniquelry
 * @Date 2018年8月14日 下午4:23:51
 * @Description 商品搜索的分页信息结果对象
 */
public class SearchResult implements Serializable {
	private List<SearchItem> itemList;// 搜索结果列表
	private long recordCount;// 总记录数
	private long pageCount;// 总页数
	public List<SearchItem> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

}
