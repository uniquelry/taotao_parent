package com.uniquelry.taotao.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.uniquelry.taotao.pojo.EasyUIDataGridResult;
import com.uniquelry.taotao.pojo.TaotaoResult;
import com.uniquelry.taotao.pojo.TbItem;
import com.uniquelry.taotao.service.ItemService;
import com.uniquelry.taotao.utils.FastDFSClient;
import com.uniquelry.taotao.utils.JsonUtils;

/**
 * @author uniquelry
 * @Date 2018年8月5日 下午8:45:51
 * @Description 
 */
@Controller
public class ItemController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
		return itemService.getItemList(page, rows);
	}
	
	/**
	 * 默认的content-type:application/json;charset=utf-8  google浏览器是支持
	 * 使用火狐浏览器 使用kindeditor的时候不支持 content-type:application/json;charset=utf-8 
	 * 解决：设置content-type:text/plain;charset=utf-8  都支持
	 * @param uploadFile
	 * @return
	 */
	@RequestMapping("/pic/upload")
	@ResponseBody
	//只需要将map对象转成JSON格式的字符串就可以了
	public String picUpload(MultipartFile uploadFile) {
		//接收上传的文件
		try {
			//取扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName=originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//上传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/fastdfs.conf");
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			url=IMAGE_SERVER_URL+url;
			//响应上传图片的url
			Map result=new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			//上传失败时的响应
			Map result=new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
	
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addItem(TbItem item,String desc) {
		TaotaoResult result=itemService.addItem(item, desc);
		return result;
	}
}
