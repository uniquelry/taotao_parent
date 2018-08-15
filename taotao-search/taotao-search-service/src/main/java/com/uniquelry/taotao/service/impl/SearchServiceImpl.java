package com.uniquelry.taotao.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniquelry.taotao.dao.SearchDao;
import com.uniquelry.taotao.mapper.SearchItemMapper;
import com.uniquelry.taotao.pojo.SearchItem;
import com.uniquelry.taotao.pojo.SearchResult;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.service.SearchService;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月14日 下午2:39:54
 * @Description 
 */
@Service
public class SearchServiceImpl implements SearchService {
	@Autowired
	private SearchItemMapper mapper;
	
	@Autowired
	private SearchDao searchDao;
	
	@Autowired
	private SolrServer solrServer;

	@Override
	public TaotaoResult importAllSearchItems() throws Exception {
		//调用mapper的方法，查询所有商品的数据
		List<SearchItem> list = mapper.getSearchItemList();
		//通过solrj将数据写入到索引库中
		//创建solrInputDocument将列表中的元素一个个放到索引库中
		for (SearchItem searchItem : list) {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", searchItem.getId().toString());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			
			//添加到索引库
			solrServer.add(document);
		}
		//提交
		solrServer.commit();
		return TaotaoResult.ok();
	}

	@Override
	public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
		//1.创建solrQuery对象
		SolrQuery query = new SolrQuery();
		//2.设置主查询条件
		if(StringUtils.isNoneBlank(queryString)) {
			query.setQuery(queryString);
		}else {
			query.setQuery("*:*");
		}
		//2.1设置过滤条件
		if(page==null) page=1;
		if(rows==null) rows=60;
		query.setStart((page-1)*rows);	// (page-1)*rows
		query.setRows(rows);
		//2.2设置默认搜索域
		query.set("df", "item_keywords");
		//2.3设置高亮
		query.setHighlight(true);
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		query.addHighlightField("item_tiele");	//设置高亮显示的域
		
		//3.调用dao方法，返回的是searchResult，只包含了总记录数和商品列表
		SearchResult searchResult = searchDao.search(query);
		
		//4.设置searchResult的总页数，返回
		Long pageCount=0l;
		pageCount=searchResult.getRecordCount()/rows;
		if(searchResult.getRecordCount()%rows>0) {
			pageCount++;
		}
		searchResult.setPageCount(pageCount);
		return searchResult;
	}
	
}
