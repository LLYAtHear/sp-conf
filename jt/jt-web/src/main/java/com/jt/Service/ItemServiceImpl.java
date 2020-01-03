package com.jt.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.anno.CacheFind;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.util.HttpClientService;
@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	private HttpClientService httpClientService;
	
	@CacheFind
	@Override
	public Item findItemById(Long itemId) {
		String url="http://manage.jt.com/web/item/findItemById?itemId="+itemId;
		return httpClientService.doGet(url, Item.class);
	}
	
	@CacheFind
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		String url="http://manage.jt.com/web/item/findItemDescById?itemId="+itemId;
		return httpClientService.doGet(url,ItemDesc.class);
	}


	
}
