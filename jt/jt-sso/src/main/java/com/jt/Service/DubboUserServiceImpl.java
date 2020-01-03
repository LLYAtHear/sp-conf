package com.jt.Service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveHashCommands.HSetCommand;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;
/**
 * 	构建接口的实现类,实现类交给dubbo管理
 * @author 000
 *
 */
@Service
public class DubboUserServiceImpl implements DubboUserService{
	/**
	 * md5加密方式加密取值多少种
	 * 32位16进制数=16^32=2^128
	 */
	@Autowired
	private JedisCluster jedisCluster;
	@Autowired
	private UserMapper usermapper;
	@Override
	public void insertUser(User user) {
		//String password =user.getPassword(); 明文
		String md5Pass=DigestUtils.md5DigestAsHex(user.getPassword().getBytes());      
		
		user.setPassword(md5Pass)
			.setEmail(user.getPhone())//暂时使用电话号码代替
			.setCreated(new Date())
			.setUpdated(user.getUpdated());
		usermapper.insert(user);
	}
	/**
	 * 1.根据username和password(明文)查询数据库
	 * 2.为null时   返回null 
	 * 3.不为null时 准备ticket数据 UUID 准备userJSON数据
	 * 将数据保存到redis服务器中,(前面设置的cookie有效期是七天,redis是也要是七天)
	 * 4.返回密钥 
	 */
	@Override
	public String findUserByUP(User user,String userIP) {
		//数据库中的密码肯定是加密模式,所以先将密码加密在进行查询
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		//根据User对象中不为null的属性充当where条件  关系符=号
		QueryWrapper<User> queryWrapper=new QueryWrapper<User>(user);
		//根据条件查询数据库记录
		User userDB = usermapper.selectOne(queryWrapper);
		//判断查询数据是否为null
		if (userDB==null) {
			return null;
		}
		//程序执行到这说明用户输入正确
		//3.1获取uuid
		String ticket = UUID.randomUUID().toString();
		//准备userJSON数据,数据必须进行脱敏处理.将敏感信息
		userDB.setPassword("123456");//1设置一个假密码显示,脱敏处理
		String userJson = ObjectMapperUtil.toJSON(userDB);
		jedisCluster.hset(ticket, "JT_USER", userJson);
		jedisCluster.hset(ticket, "JT_USER",userIP);
		jedisCluster.expire(ticket, 7*24*3600);
		
		//用户名与ticket信息绑定
		jedisCluster.setex("JT_USER_"+user.getUsername(), 7*24*3600, ticket);
		//
		return ticket;
	}

}
