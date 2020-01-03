package com.jt.web;

import javax.xml.bind.annotation.W3CDomHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item/")
public class webController {
	
	
	@Autowired
	private ItemService itemService; 
	@RequestMapping("findItemById")
	public Item findItemById(Long itemId) {
		return itemService.findItemById(itemId);
	}
	
	@RequestMapping("findItemDescById")
	public ItemDesc findItemDecById(Long itemId) {
		return itemService.findItemDescById(itemId);
	}

//	//获取商品信息
//	@RequestMapping("findItemById")
//	public Item findItemById(Long itemId) {
//		
//		 Item info = itemService.findItemById(itemId);
//		return info;
//	}
//	//获取商品详情信息
//	@RequestMapping("findItemDescById")
//	public ItemDesc findItemDescById(Long itemId) {
//		
//		ItemDesc descinfo = itemService.findItemDescById(itemId);
//		return descinfo; 
//	}
}
