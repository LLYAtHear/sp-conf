package com.jt.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;

public class ObjectToString {
	private static final ObjectMapper MAPPER=new ObjectMapper();
	
	/**
	 * 	
	 * ObjectMapper工具api
	 * 	具体方法writeValueAsString
	 * @throws IOException 
	 */
	
	@Test
	public void testObj() throws IOException  {
		ItemDesc desc = new ItemDesc();
		desc.setItemId(1000L)
		.setItemDesc("描述信息")
		.setCreated(new Date())
		.setUpdated(desc.getCreated());
		//对象转字符串writeValueAsString
		String json = MAPPER.writeValueAsString(desc);
		System.out.println(json);
		//Json转对象readValues
		ItemDesc desc2 = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(desc2);
		
		
		
	}
	/**
	 * 将list集合转化为json
	 * 结构:[{k:v,k:v...}]
	 */
	@SuppressWarnings("unchecked") //压制警告
	@Test
	public void testList1() throws IOException {
		ItemDesc desc2=new ItemDesc();
		desc2.setItemId(1000L)
		.setItemDesc("描述信息")
		.setCreated(new Date())
		.setUpdated(desc2.getCreated());
		ItemDesc desc3=new ItemDesc();
		desc3.setItemId(100L)
		.setItemDesc("描述信息233")
		.setCreated(new Date())
		.setUpdated(desc3.getCreated());
		List<ItemDesc> list = new ArrayList<>();
		list.add(desc2);
		list.add(desc3);
		//集合转化为Json
		String json = MAPPER.writeValueAsString(list);
		System.out.println(json);
		//json转化为list集合
		@SuppressWarnings("rawtypes")
		List list2 = MAPPER.readValue(json, list.getClass());
		System.out.println(list2);
	}
}
