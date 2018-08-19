package com.uniquelry.taotao.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.uniquelry.taotao.pojo.SearchItem;
import com.uniquelry.taotao.pojo.SearchResult;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月14日 下午4:28:05
 * @Description 从索引库中搜索商品的dao
 */
@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 根据查询条件查询商品的结果集
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public SearchResult search(SolrQuery query) throws Exception{
		SearchResult searchResult = new SearchResult();
		//执行查询
		QueryResponse response = solrServer.query(query);
		//获取结果集
		SolrDocumentList results = response.getResults();
		//设置searchResult的总记录数
		searchResult.setRecordCount(results.getNumFound());
		//遍历结果集
		List<SearchItem> itemList=new ArrayList<>();
		
		//取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for (SolrDocument solrDocument : results) {
			//将solrdocument中的属性一个个设置到searchitem中
			SearchItem item= new SearchItem();
			item.setId(Long.parseLong(solrDocument.get("id").toString()));
			//取高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			//判断list是否为空
			String gaoliangstr="";
			if(list!=null&&list.size()>0) {
				//有高亮
				gaoliangstr=list.get(0);
			}else {
				gaoliangstr=solrDocument.get("item_title").toString();
			}
			item.setTitle(gaoliangstr);
			item.setSell_point(solrDocument.get("item_sell_point").toString());
			item.setPrice(Long.parseLong(solrDocument.get("item_price").toString()));
			item.setImage(solrDocument.get("item_image").toString());
			item.setCategory_name(solrDocument.get("item_category_name").toString());
			//item.setItem_desc(solrDocument.get("item_desc").toString());
			
			//将searchitem添加到searchresult中的itemlist属性中
			itemList.add(item);
			
		}
		//设置searchResult的属性
		searchResult.setItemList(itemList);
		return searchResult;
	}
	
}
