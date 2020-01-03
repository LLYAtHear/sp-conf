package com.jt.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Pointcut;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;

@RestControllerAdvice
//相当于@Component @Aspect @Pointcut
public class SystemExeAOP {
	/**
	 * 如果程序出错,应该返回SysResult.fail();将数据装华为Json
	 * 在Controller中如果出现问题则执行此业务
	 */
	
	/**
	 * 由于jsonp的请求方式,返回值必须callback(json)
	 * 思路:利用request对象动态获取callback
	 * 如果用户参数是get请求,且参数名称为callback
	 * 则使用jsonp的方式返回
	 * 如果请求中没有callback,则按照正常的异常返回
	 * @param e
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public Object fail(RuntimeException e,HttpServletRequest request) {
		e.printStackTrace();
		String callback = request.getParameter("callback");
		if(StringUtils.isEmpty(callback)) {
			return SysResult.fail();
		}else {
			//采用jsonp的方式调用服务器
			return new JSONPObject(callback, SysResult.fail());
		}

	}
//	public SysResult fail(RuntimeException e) {
//		
//		e.printStackTrace();
//		return SysResult.fail();
//	}
}
