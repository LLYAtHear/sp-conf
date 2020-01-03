package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.http.cookie.CookieSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
/**
 * 完成用户的业务操作
 * @author 000
 *
 */
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;
@Controller
@RequestMapping("/user")
public class UserController {
	
	//引入中立的接口,创建代理对象
	@Reference(check = false)
	private DubboUserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 实现通用页面的跳转
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/{moduleName}")
	public String toModule(@PathVariable String moduleName) {
		return moduleName;
	}
	
	/**
	 * 业务功能:
	 * 实现用户信息的新增,返回系统返回值的对象SysResult
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/doRegister")
	public SysResult insertUser(User user) {
		userService.insertUser(user);
		return SysResult.success();
	}
	/**
	 * 准备一个cookie对象
	 * 
	 * Cookie.setMaxAge(expiry);
	 * expiry>0 为cookie设置超时时间单位秒
	 * expiry=0 删除Cookie
	 * expiry=-1 会话结束之后删除
	 * @param user
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLogin(User user,HttpServletResponse response,HttpServletRequest request) {
		//获取userIP
		String userIP = IPUtil.getIpAddr(request);
		
		
		
		//当用户名密码正确,返回ticket,不正确则不需要返回,此处需要判断
		String ticket=userService.findUserByUP(user,userIP);
		//判断数据是否为null
		if (StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		
		
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setMaxAge(7*24*3600);  //设置cookie的生存时间
		cookie.setPath("/");	//设置cookie数据读取的范围     设置/()根目录时,在同一页面下的所有路径方法都可获取cookie数据,为/user时,只能在user下的路径方法能获取
		cookie.setDomain("jt.com");  //设定cookie的共享,在jt.com的所有服务器都可接受到数据,
		response.addCookie(cookie);
		return SysResult.success();  //正确返回
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		//1.获取Cookie数据  JT_TICKET JT_USER
		Cookie[] cookies = request.getCookies();
		//判断cookie是否有数据,有的话获取从cookie中获取数据
		String jtTicket=null;
		String jtUser=null;
		if (cookies!=null &&cookies.length>0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JT_TICKET")) {
					jtTicket = cookie.getValue();
				}
				if (cookie.getName().equals("JT_USER")) {
					jtUser=cookie.getValue();
				}
			}			
		}
		//2.判断cookie是否为null
		if (!StringUtils.isEmpty(jtTicket)) {
			//删除redis
			jedisCluster.del(jtTicket);
			//删除cookie
			CookieUtil.deleteCookie("JT_TICKET", "/", "jt.com", response);
		}
		
		return "";
		
	}
}
