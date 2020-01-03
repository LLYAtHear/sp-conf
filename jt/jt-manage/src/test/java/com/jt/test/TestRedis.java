package com.jt.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.tomcat.jni.Pool;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	private Jedis jedis;
	
	@Before
	public void init() {
		jedis=new Jedis("192.168.207.128",6379);
	}
	
	/**
	 * 1.执行关闭防火墙命令 service iptables stop
	 * 2.redis-server redis.conf(默认启动权限不正确)
	 * 3.检查配置文件3处 ip绑定  保护模式关闭 后台启动开启
	 */
	@Test
	public void testString() {
		jedis.set("1908", "hhxxttxs");
		System.out.println(jedis.get("1908"));
		
		//如果key已经存在,则不允许操作redis
		//原理:只能操作不存在的key 0失败/1成功
		Long flag = jedis.setnx("1909", "jtzs");
		System.out.println(jedis.get("1909"));
		System.out.println("标识符:"+flag);
		
		//为数据添加超时时间
		//jedis.set("1908", "abc");
		//jedis.expire("1908", 10);
		jedis.setex("1910", 10, "xxs");
		System.out.println(jedis.get("1910"));
		//jedis.psetex(key, milliseconds, value)
		
	}
	/**
	 * 要求:setnx和setex的方法要求同时完成
	 * 实际用法:实现redis分布式锁的关键
	 * 
	 *	成功返回ok不成功返回字符串null
	 */
	@Test
	public void textNXEX() {
		String result = jedis.set("abc", "xxsrrtg", "NX", "EX", 20);
		System.out.println(result);
	}
	
	/**
	 * 操作hash类型
	 */
	@Test
	public void testHash() {
		jedis.hset("user", "name", "a");
		jedis.hset("user", "id", "1");
		Map<String, String> map = jedis.hgetAll("user");
		System.out.println(map);
	}
	/**
	 * 操作list集合
	 * 队列(先进先出)和栈(后进先出)
	 *特点:取出数据,内存中不会保存该数据 
	 */
	@Test
	public void testList() {
		jedis.lpush("list", "1","2","3","4","5");
		System.out.println(jedis.rpop("list"));
	}
	
	/**
	 * 事务操作
	 */
	@Test
	public void testTx() {
		Transaction transaction = jedis.multi();
		try {
			transaction.set("1", "2");
			transaction.set("3", "4");
			transaction.exec();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();
		}
		
		
	}
	
	
	/**
	 * 测试分片
	 */
	@Test
	public void testShards() {
		
		List<JedisShardInfo> shards=new ArrayList<>();
		shards.add(new JedisShardInfo("192.168.207.128",6379));
		shards.add(new JedisShardInfo("192.168.207.128",6380));
		shards.add(new JedisShardInfo("192.168.207.128",6381));

		//操作分片的工具API
		ShardedJedis jedis = new ShardedJedis(shards);
		jedis.set("1910", "分片测试案例");
		System.out.println(jedis.get("1910"));
		
	}
	
	@Test
	public void testSentinel() {
		Set<String> sentinels=new HashSet<>();
		sentinels.add("192.168.207.128:26379");
		JedisSentinelPool pool=new JedisSentinelPool("mymaster", sentinels);

		Jedis jedis=pool.getResource();
		jedis.set("adc", "aaaa");
		System.out.println(jedis.get("adc"));
	}
	
	@Test
	public void testCluster() {
		
		Set<HostAndPort> nodes=new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.207.128", 7000));
		nodes.add(new HostAndPort("192.168.207.128", 7001));
		nodes.add(new HostAndPort("192.168.207.128", 7002));
		nodes.add(new HostAndPort("192.168.207.128", 7003));
		nodes.add(new HostAndPort("192.168.207.128", 7004));
		nodes.add(new HostAndPort("192.168.207.128", 7005));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		
		//hash槽算法
		jedisCluster.set("1911", "redis集群搭建完成");
		System.out.println(jedisCluster.get("1911"));
	}
	
	
	
}
