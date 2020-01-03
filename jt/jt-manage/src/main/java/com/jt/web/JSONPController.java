package com.jt.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@RestController
public class JSONPController {
	
	
	//@RequestMapping("/web/testJSONP")
	public String jsonOld(String callback) {
		ItemDesc itemDesc=new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("商品信息");
		String json=ObjectMapperUtil.toJSON(itemDesc);
		return callback+"("+json+")";
	}
	
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		ItemDesc itemDesc=new ItemDesc();
		itemDesc.setItemId(100L)
				.setItemDesc("商品信息");
		return new JSONPObject(callback, itemDesc);
	}
}
