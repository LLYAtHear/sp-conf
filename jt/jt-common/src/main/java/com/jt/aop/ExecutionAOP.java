//package com.jt.aop;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Component
//@Aspect
//public class ExecutionAOP {
//	//如果程序出现了异常,则需要拦截异常,打印日志
//	@AfterThrowing(pointcut = "execution(* com.jt.service..*.*(..))",
//			throwing="throwable")
//	public void afterThrow(Throwable throwable,JoinPoint joinpoint) {
//			
//		Class<?> targetClass=joinpoint.getTarget().getClass();
//		String methodname = joinpoint.getSignature().getName();
//		Class<?> throwClass = throwable.getClass();
//		String message = throwable.getMessage();
//		
//		System.out.println("目标对象类型:"+targetClass);
//		System.out.println("目标方法:"+methodname);
//		System.out.println("异常类型:"+throwClass);
//		System.out.println("异常信息:"+message);
//	}
//}
