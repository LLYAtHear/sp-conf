package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.annotation.IdType;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	
	/**
	 * 根据ItemCatId查询数据
	 * url:/item/cat/queryItemName
	 * 参数:{itemCatId.val}
	 * 返回值:itemCatName
	 */
	@RequestMapping("/queryItemName")
	public String findItemCatNameById(Long itemCatId) {
		ItemCat itemCat=itemCatService.findItemCatById(itemCatId);
		
		return itemCat.getName();
	}
	
	
	/**
	 * 商品分类信息List<ItemCat>树形结构展现List<EasyUITree>
	 * url:/item/cat/list
	 * 参数: xxxx 
	 * 返回值:List<EasyUITree>
	 * 
	 * @RequestParam用法
	 */
	
	@RequestMapping("/list")
	public  List<EasyUITree> findItemCat(@RequestParam(name="id",defaultValue = "0")Long parentId){

		return itemCatService.findItemCat(parentId);
		//return itemCatService.findItemCatCache(parentId);
	}
	
}
