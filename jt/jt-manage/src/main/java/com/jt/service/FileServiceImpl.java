package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.ibatis.javassist.expr.NewArray;
import org.aspectj.apache.bcel.classfile.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVo;
@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{
	@Value("${image.localDir}")
	private String localDir;//="D:/JT/FileLocation";
	@Value("${image.httpUrl}") 
	private String httpUrl;//="http://image.jt.com";

	/**
	 * 	过程
	 * 	真实图片信息:abc.jpg
	 * 	实现文件上传思路:
	 * 	1.判断是否为图片类型  jpg/png/gif
	 *  2.判断是否为恶意程序
	 *  3.图片必须分目录存储 按时间yyyy/mm/dd
 	 *  4.防止图片重名  UUID.类型
	 */
	@Override
	public ImageVo upload(MultipartFile uploadFile) {
		
		//1.获取图片名称
		String imageName = uploadFile.getOriginalFilename();
		//1.1 防止后缀为大写,转成小写
		imageName = imageName.toLowerCase();
		//1.2 判断是否为图片类型
		if (!imageName.matches("^.+\\.(jpg|png|gif)$")) {
			return ImageVo.fail();
		}
		//2.是否为恶意程序           主要判断图片的属性宽度
		//2.1通过一个工具api,得到图片的宽和高使用ImageIo
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int width=bufferedImage.getWidth();
			int heigth=bufferedImage.getHeight();
			
			if (width==0||heigth==0) {
				return ImageVo.fail();
			}
			
			//3.实现分目录存储,本地磁盘路径+日期目录   工具与APISimpleDateFormat
			String dateDir=new SimpleDateFormat("/yyyy/MM/dd/")
							.format(new Date());
			String fileDirPath= localDir+dateDir;
			File fileDir=new File(fileDirPath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			//4.防止文件重名,使用UUID.类型
			String uuid = UUID.randomUUID().toString();
			int index = imageName.lastIndexOf(".");
			//jpg
			String fileType=imageName.substring(index);
			String fileName = uuid+fileType;
			//5.实现上传文件  目录文件+文件名称
			String realFilePath = fileDirPath+fileName;
			uploadFile.transferTo(new File(realFilePath));
			System.out.println("文件上传成功");
			
			//6.拼接图片虚拟地址
			String url=httpUrl+dateDir+fileName;
			return new ImageVo(0, url, width, heigth);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ImageVo.fail();
		}
		
		
	
	}

}
