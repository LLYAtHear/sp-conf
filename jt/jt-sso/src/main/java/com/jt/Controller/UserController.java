package com.jt.Controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.Service.UserService;
import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private JedisCluster jedisCluster;
	@Autowired
	private UserService userService;
	@RequestMapping("/findAll")
	public List<User> findAll() {
		return userService.findAll();
	}
	
	
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String params,
								 @PathVariable Integer type,
								 @PathVariable String callback) {
		//定义boolean哦判断用户是否存在
		Boolean flag=userService.findUserByType(params,type);
		SysResult sysResult= SysResult.success(flag);
		return new JSONPObject(callback, sysResult);
	}
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,
			HttpServletRequest request,
			HttpServletResponse response,
			String callback) {
		String IP = IPUtil.getIpAddr(request);
		Map<String,String> map = jedisCluster.hgetAll(ticket);
		
		JSONPObject object = null;
		//1.校验IP是否有效.
		if(!IP.equals(map.get("JT_USER_IP"))) {
			
			//IP地址不正确.
			object = new JSONPObject(callback,SysResult.fail());
			//删除cookie信息
			CookieUtil.deleteCookie("JT_TICKET","/","jt.com", response);
			return object;
		}
		
		//2.校验ticket数据信息.
		String userJSON = map.get("JT_USER");
		if(StringUtils.isEmpty(userJSON)) {
			
			//IP地址不正确.
			object = new JSONPObject(callback,SysResult.fail());
			CookieUtil.deleteCookie("JT_TICKET","/","jt.com", response);
			return object;
		}
		
		//3.表示校验成功
		object = new JSONPObject(callback, SysResult.success(userJSON));
		return object;
	}

}
