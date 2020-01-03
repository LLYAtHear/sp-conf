package com.jt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 实现商品列表展现
	 * url地址:/item/query
	 * 	参数说明:page=1&rows=10
	 * 返回值i:EasyUITable的json数据
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		return  itemService.findItemByPageOld(page,rows);
		
	}
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		try {
			itemService.saveItem(item,itemDesc);
			return SysResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
		
	}
	
	
	/**
	 * 完成商品的更新
	 * url:/item/update
	 * 参数:key=value&...
	 * 返回值:SysResult
	 */
	
	@RequestMapping("/update")
	public SysResult updata(Item item) {
		
		itemService.updata(item);
		return SysResult.success();
	}
	
	
	/**
	 * 完成商品的删除
	 * url:/item/delete
	 * 参数:"ids":ids  逗号,进行分割
	 * 返回值:SysResult
	 * 
	 * SpringMVC底层取值原理
	 *  底层:servlet
	 *  利用request域对象取值,request.getParameter();
	 */
	
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	
	//下架
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		Integer status=2;
		itemService.updateStatus(ids,status);
		return SysResult.success();
	}
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		Integer status=1;
		itemService.updateStatus(ids,status);
		return SysResult.success();	
	}
	
	/**
	 * 获取商品规格
	 * url:/item/query/item/desc/
	 * 参数:
	 * 返回值:SysResult 包含商品详细信息
	 * JSON格式{status:200,msg:"成功",data:{k:v,k:v,itemDesc:"html代码"}}
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescById(@PathVariable Long itemId) {
		
		ItemDesc itemDesc=itemService.findItemDescById(itemId);
		return SysResult.success(itemDesc);
	}
}
