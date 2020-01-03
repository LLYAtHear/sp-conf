package com.jt.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //标识当前注解的生命周期
@Target({ElementType.METHOD})//标识注解对方法生效
public @interface CacheFind {

	/**
	 * 注解中配置的内容
	 * 	1.简单 用户使用简单
	 *  2.实用
	 *  要求:key不同的业务一定不能重复
	 *  规则: key默认为"",用户添加了key,则使用用户的
	 *  			  若用户没有添加,则生成策略:包名.类名.方法名::第一个参数
	 *  
	 *  添加过期时间:如果不为0,则需要添加过期时间setex
	 */
	
	String key() default "";
	int seconds() default 0;
}
