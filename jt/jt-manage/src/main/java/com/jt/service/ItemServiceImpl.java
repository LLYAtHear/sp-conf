package com.jt.service;

import java.sql.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	@Autowired
	private ItemMapper itemMapper;
	
	/**
	 * 分页查询数据
	 * select * from tb_item limit 起始页,记录数
	 * 第一页
	 * select * from tb_item limit 0,20               含头不含尾
	 * 第二页
	 * select * from tb_item limit 20,20
	 * 第n页
	 * select * from tb_item limit (page-1)*rows, rows
	 */
	@Override
	public EasyUITable findItemByPageOld(Integer page, Integer rows) {
		//开始毫秒数
		Long startTime=System.currentTimeMillis();
		//查询记录总数
		int total=itemMapper.selectCount(null);
		//分页查询记录
		int start=(page-1)*rows;
		List<Item> items=itemMapper.findItemByPage(start,rows);
		Long endTime=System.currentTimeMillis();
		System.out.println("执行时间:"+(endTime-startTime));
		//自己完成MP分页功能 1.完成API  2.需要序列化器 编辑配制类 
		//IPage<Item> iPage = new Page<>();
		//itemMapper.selectPage(null, null);
		return new EasyUITable(total,items);
	}
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		Long startTime=System.currentTimeMillis();
		
		Page<Item> itemPage=new Page<>(page,rows);
		QueryWrapper<Item> queryWrapper=new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		IPage<Item> iPage = itemMapper.selectPage(itemPage, queryWrapper);
		
		int count=new Long(iPage.getTotal()).intValue();
		List<Item> itemList = iPage.getRecords();
		
		Long endTime=System.currentTimeMillis();
		System.out.println("执行时间:"+(endTime-startTime));
		return new EasyUITable(count,itemList);
	}

	
	/***
	 * 关于主键自增赋值问题
	 * 说明:一般情况下,当数据入库后,才会动态的生成主键信息
	 * 如果在关联操作时,需要用到主键信息,一般id为null,需要动态查询
	 * 
	 * 思路一:先入库, 在查询,获取id 98正确
	 * 思路二:先入库,动态回传主键信息 mybatis配合MP实现
	 * 
	 * 一般思路:需要开启mybatis数据的主键回传
	 */
	@Override
	@Transactional 			//事务处理
	public void saveItem(Item item,ItemDesc itemDesc) {
		
		/**
		 * 商品上架了需要设置商品的状态以及上架.更新的时间
		 * 
		 * 避免出现商品入库后前台显示添加失败,所以添加事务,失败后回滚
		 */
		item.setStatus(1)
		.setCreated(new Date())
		.setUpdated(item.getCreated());
		
		itemMapper.insert(item);  //动态赋值Id
		//当向tb_item表插入时item没有id 插入后分配id  
		//当用MP方法入库时会回传id 插入完成后item的id是数据库中的id

		
		
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
		
	}

	@Override
	public void updata(Item item) {
		
		item.setUpdated(new Date());
		itemMapper.updateById(item);
	}

	@Override
	public void deleteItems(Long[] ids) {
		
		//itemMapper.deleteByIds(ids);
		 List<Long> idList=Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
	}

	
	/**
	 * 方式一:
	 * sql:	Sql: update tb_item status = #{status},updated=#{date}
	 * 		 where id in (101,102)

	 * 方式二:MP
	 * entity:修改的数据
	 * updateWrapper:修改的条件.条件选择器
	 */
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		
		
		Item item=new Item();
		item.setStatus(status)
			.setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		updateWrapper.in("id", ids);
		itemMapper.update(item,updateWrapper);
		
	}
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}
	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}
	
	
	

	
	
	
	
	
	
	
	
}
