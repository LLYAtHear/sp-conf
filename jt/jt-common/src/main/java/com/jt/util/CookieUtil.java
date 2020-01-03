package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 删除Cookie的API
 * @author 000
 *
 */
public class CookieUtil {
	//1.删除Cookie信息
	public static void deleteCookie(String cookieName,String path,String domain,HttpServletResponse response) {
		
		Cookie cookie=new Cookie(cookieName, "");
		cookie.setMaxAge(0);
		cookie.setPath(path);
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}
}
