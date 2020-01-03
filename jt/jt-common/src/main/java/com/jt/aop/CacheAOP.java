package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.CacheFind;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//定义缓存切面
@Component //万能注解,交给spring容器管理
@Aspect  //自定义切面
public class CacheAOP {
	
	
	@Autowired(required = false)
	private JedisCluster jedis;
	//private Jedis jedis; //哨兵的jedis
	//private ShardedJedis jedis;
	//private Jedis jedis;
	/**
	 * 环绕通知的语法
	 * 返回值类型:任意类型用Obj包裹
	 * 参数说明:必须包含且第一个位置是第一个  
	 * ProceedingJoinPoint
	 * 
	 * 通知标识:
	 * 		1.@Around("切入点表达式")
	 * 		2.@Around("切入点()")
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		//定义数据的返回值
		Object result=null;
		
		String key= getkey(joinPoint,cacheFind);
		String value = jedis.get(key);
		if (StringUtils.isEmpty(value)) {
			
			try {
				//缓存数据为null,查询数据库
				result=joinPoint.proceed();
				String json = ObjectMapperUtil.toJSON(result);
				
				if (cacheFind.seconds()>0) {
					//需要添加超时时间
					jedis.setex(key, cacheFind.seconds(),json);
				}else {
					jedis.set(key, json);
				}
				System.out.println("AOP实现数据库查询");
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		}else {
			//缓存数据不为null,需要将json转化为对象
			Class returnType=getReturnType(joinPoint);
			result=ObjectMapperUtil.toObject(value, returnType);
			System.out.println("AOP实现缓存查询");
		}
		
		return result;
	}
	
	private Class getReturnType(ProceedingJoinPoint joinPoint) {
		//实现了方法对象的封装
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		return signature.getReturnType();
	}

	
	/**
	 * 获取key数据
	 * 		!null以用户的数据key为主
	 * 		null包名.类名.方法名::参数
	 * @param joinPoint
	 * @param cacheFind
	 * @return
	 * 
	 * signature对所有的方法进行包装
	 */
	private String getkey(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		String key=cacheFind.key();
		if (StringUtils.isEmpty(key)) {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object firstArgs = joinPoint.getArgs()[0];
			return className+"."+methodName+"::"+firstArgs;
			
		}else {
			//以用户的数据为主
			return key;
		}
	}
}
