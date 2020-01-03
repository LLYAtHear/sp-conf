package com.jt.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.language.bm.Rule.Phoneme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.vo.SysResult;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	@Override
	public List<User> findAll() {
		return userMapper.selectList(null);
	}
	/**
	 * param  需要校验的数据
	 * type   1 username、2 phone、3 email
	 * 	业务说明:根据参数查询数据库
	 * sql: select * from tb_user  字段 = #{param}
	 * 	结果返回: 如果存在返回true  不存在返回 false
	 */
	@Override
	public Boolean findUserByType(String param, Integer type) {
		//1.类型转化为字段
		Map<Integer, String> map=new HashMap<Integer, String>();
		map.put(1, "username");
		map.put(2, "Phone");
		map.put(3, "email");
		String column = map.get(type);
		
		//2.校验用户是否存在
		QueryWrapper<User> queryWrapper=new QueryWrapper<User>();
		queryWrapper.eq(column, param);
		User user = userMapper.selectOne(queryWrapper);
		
		return user==null?false:true;
	}

}
