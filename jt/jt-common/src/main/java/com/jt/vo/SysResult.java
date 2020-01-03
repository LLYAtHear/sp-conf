package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 该类为系统级返回值VO对象,判断后台操作是否正确
 * @author 000
 *
 */
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult {
	private Integer status;//200-success  201-失败
	private String msg; //相应提示信息
	private Object data;//服务器响应的数据
	
	
	/**
	 * 为了方便调用,封装工具api static
	 */
	public  static SysResult success() {
		return new SysResult(200,"服务器处理成功",null);
	}
	
	public  static SysResult success(Object data) {
		return new SysResult(200,"服务器处理成功",data);
	}
	/**
	 * 此处出现bug,当用户只想传送一个字符串类型的data用来保存数据时,系统
	 * 会自动识别为msg,造成错误,所以在规定传送msg时必须传送data,没有则
	 * 传送null值,这样不会造成判断错误
	 */
	public  static SysResult success(String msg,Object data) {
		return new SysResult(200,msg,data);
	}
	
	public  static SysResult fail() {
		return new SysResult(201,"服务器处理失败",null);
	}
	
}
