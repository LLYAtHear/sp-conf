package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RunTimeAOP {
	
	
	@Around("execution(* com.jt.service..*.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) {
		
		Long startTime=System.currentTimeMillis();
		Object obj=null;
		//执行目标方法
		
		try {
			obj=joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		Long endTime=System.currentTimeMillis();
		Class<?> targetclass = joinPoint.getTarget().getClass();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("执行对象类型:"+targetclass);
		System.out.println("执行对象方法:"+methodName);
		System.out.println("执行时间:"+(endTime-startTime));
		return obj;
	}
}
