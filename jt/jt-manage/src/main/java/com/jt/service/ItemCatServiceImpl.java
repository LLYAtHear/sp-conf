package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jt.anno.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;

@Service
public class ItemCatServiceImpl implements ItemCatService{
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	//类似于懒加载,什么时候用,什么时候调用
	@Autowired(required = false)
	private Jedis jedis;
	

	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		
		return itemCatMapper.selectById(itemCatId);
	}


	@CacheFind
	@Override
	public List<EasyUITree> findItemCat(Long parentId) {
		List<ItemCat> cartList = findItemCatList(parentId);
		ArrayList<EasyUITree> treeList = new ArrayList<>(cartList.size());
		
		for (ItemCat itemCat : cartList) {
			Long id = itemCat.getId();
			String name = itemCat.getName();
			//规定:如果是父级节点closed,否则open (EasyUI规则)
			//此处使用三目运算法
			String state= itemCat.getIsParent()?"closed":"open";
			EasyUITree tree = new EasyUITree(id,name,state);
			treeList.add(tree);
		}
		
		return treeList;
	}
	
	public List<ItemCat> findItemCatList(Long parentId){
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		return itemCatMapper.selectList(queryWrapper);
	}



	//1.查询缓存k-v  k是一个变量, v List<EasyUITree>的json格式
	@SuppressWarnings("unchecked") //压制警告
	// ::意思是后面是一个参数
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		String key="com.jt.service.ItemCatServiceImpl.findItemCatCache::"+parentId;
		String value = jedis.get(key);
		List<EasyUITree> treeList = new ArrayList<>();
		Long start=System.currentTimeMillis(); 
		if (StringUtils.isEmpty(value)) {  //如果redis缓存为空
			//第一次调用   查询数据库
			
			treeList = findItemCat(parentId);
			if (treeList.size()>0) {
				String json = ObjectMapperUtil.toJSON(treeList);
				jedis.set(key, json);
			}
			Long end=System.currentTimeMillis();
			System.out.println("查询数据库时间为:"+(end-start));
		}else {
			//用户不是第一次查询,查询缓存redis
			treeList=ObjectMapperUtil.toObject(value, treeList.getClass());
			Long end=System.currentTimeMillis();
			System.out.println("查询数据库时间为:"+(end-start));
		}
		return treeList;
	}
}
