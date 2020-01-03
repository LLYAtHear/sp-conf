package com.jt.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.service.FileServiceImpl;
import com.jt.vo.ImageVo;

@RestController
public class fileController {
	
	@Autowired
	private FileService fileService;
	/**
	 * w文件上传时参数名称必须一致
	 * D:\JT\FileLocation
	 * Mult
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping("/file")
	public  String  file(MultipartFile fileImage) throws IllegalStateException, IOException {
		//1.创建文件目录
		File fileDir=new File("D:/JT/FileLocation");
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		//2.获取文件名字
		String name = fileImage.getOriginalFilename();
		//3.准备文件路径
		String filepath="D:/JT/FileLocation/"+name;
		//4实现文件上传
		fileImage.transferTo(new File(filepath));
		System.out.println("上传成功 ");
		return "上传成功";
	}
	
	/**
	 * 实现商品文件上传
	 * url:/pic/upload
	 * 参数:uploadFile名称
	 * 结果:ImageVo对象
	 */
	
	@RequestMapping("/pic/upload")
	public ImageVo upload(MultipartFile uploadFile) {
		return fileService.upload(uploadFile);
	}
}
