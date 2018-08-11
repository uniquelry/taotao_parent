package com.uniquelry.taotao.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.uniquelry.taotao.utils.FastDFSClient;

/**
 * @author uniquelry
 * @Date 2018年8月7日 上午10:10:48
 * @Description 
 */
public class TestFastDFS {
	
	@Test
	public void TestUploadFile() throws Exception {
		//1.项工程中添加fastdfs_client的jar包
		//2.创建一个配置文件，配置tracker的服务器地址
		//3.加载配置文件
		ClientGlobal.init("E:\\workspace\\taotao-parent\\taotao-manager-web\\src\\main\\resources\\resource\\fastdfs.conf");
		//4.创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//5.使用TrackerClient对象获得TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//6.创建一个StorageServer的引用null
		StorageServer storageServer=null;
		//7.创建一个StorageClient对象。TrackerClient，TrackerServer两个参数
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//8.使用StorageClient对象上传文件
		String[] strings = storageClient.upload_file("F:\\WebPic\\headerpig.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
	@Test
	public void TestFastDFSClient() throws Exception {
		FastDFSClient fastDFSClient=new FastDFSClient("E:\\workspace\\taotao-parent\\taotao-manager-web\\src\\main\\resources\\resource\\fastdfs.conf");
		String string = fastDFSClient.uploadFile("F:\\WebPic\\cat_121.21212121212px_1165134_easyicon.net.png");
		System.out.println(string);
	}
}
