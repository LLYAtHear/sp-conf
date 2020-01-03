package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVo {
	
	private Integer error;  //0 上传正确  1 上传错误
	private String url;    //图片虚拟的地址
	private Integer width;
	private Integer height;
	
	
	//根据分析,只需要定义一个失败的方法
	public static ImageVo fail() {
		return new ImageVo(1,null,null,null);
	}
}
