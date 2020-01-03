package com.jt.controller;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	/**
	 * 当{abc}中参数名字与方法参数不一致,可标识以下
	 * @PathVariable(value="abc") String module
	 * @param module
	 * @return
	 */
	@RequestMapping("/page/{module}")
	public String module(@PathVariable String module) {
		
		return module;
	}
	
	
}
