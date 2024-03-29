package com.jt.util;

import com.jt.pojo.User;
//利用threadLocal动态获取用户id
public class UserThreadLocal {
	private static ThreadLocal<User> thread=new ThreadLocal<>();
	
	public static void set(User user) {
		thread.set(user);
	}
	public static User get() {
		return thread.get();
	}
	public static void remove() {
		thread.remove();
	}
	
}
