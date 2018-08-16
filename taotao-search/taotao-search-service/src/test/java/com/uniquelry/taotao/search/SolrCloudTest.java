package com.uniquelry.taotao.search;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * @author uniquelry
 * @Email uniquelry@qq.com
 * @Date 2018年8月16日 下午3:49:01
 * @Description solrCloud
 */
public class SolrCloudTest {
	@Test
	public void testAdd() throws Exception {
		//1.创建solrServer，集群的实现类
		//指定zookeeper集群的节点列表字符串
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.128:2182,192.168.25.128:2183,192.168.25.128:2184");
		//2.设置默认的搜索的collection，默认的搜索索引库（不是core对应的，是指collection索引集合）
		cloudSolrServer.setDefaultCollection("collection2");
		//3.创建solrInputDocument对象
		SolrInputDocument solrInputDocument = new SolrInputDocument();
		//4.添加域到文档
		solrInputDocument.addField("id", "testCloudId");
		solrInputDocument.addField("item_title", "今天是个好日子");
		//5.将文档提交到索引库
		cloudSolrServer.add(solrInputDocument);
		//6.提交
		cloudSolrServer.commit();
	}
}
