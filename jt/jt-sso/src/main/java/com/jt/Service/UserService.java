package com.jt.Service;

import java.util.List;

import com.jt.pojo.User;
import com.jt.vo.SysResult;

public interface UserService {

	List<User> findAll();

	Boolean findUserByType(String params, Integer type);

}
