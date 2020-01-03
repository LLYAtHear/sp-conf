package com.jt.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{
	
	
	@Select("select * from tb_item order by updated desc limit #{start},#{rows} ")
	List<Item> findItemByPage(int start, int rows);
	
	/**
	 * 封装了一个Map集合
	 * 关于
	 * @param ids
	 */
	void deleteByIds(@Param("ids")Long[] ids);
	
}
