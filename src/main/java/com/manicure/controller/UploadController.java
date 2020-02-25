package com.manicure.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

@RestController
public class UploadController {
	
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file){	
		
		//1.根据文件获取扩展名
		String originalFilename = file.getOriginalFilename();
		System.out.println("文件名："+originalFilename);
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
		try {
			//2.创建一个 FastDFS 的客户端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			//3、执行上传处理
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
			//4、拼接返回的 url 和 ip 地址，拼装成完整的 url
			String url = FILE_SERVER_URL + path;
			System.out.println("图片全路径为："+url);
			return new Result(true,url);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(true,"上传失败");
		}
	}

}
