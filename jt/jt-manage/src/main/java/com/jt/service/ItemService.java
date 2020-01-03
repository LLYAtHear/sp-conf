package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

	EasyUITable findItemByPageOld(Integer page, Integer rows);
	EasyUITable findItemByPage(Integer page, Integer rows);
	void saveItem(Item item,ItemDesc itemDesc);
	void updata(Item item);
	void deleteItems(Long[] ids);
	void updateStatus(Long[] ids, Integer status);
	
	ItemDesc findItemDescById(Long itemId);
	Item findItemById(Long itemId);
}
