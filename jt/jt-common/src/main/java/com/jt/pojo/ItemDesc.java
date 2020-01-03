package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data  //tostring只能包含当前类中的属性
@Accessors(chain = true)
public class ItemDesc extends BasePojo{
	
	private Long ItemId;	//id必须与商品id号一致
	private String ItemDesc;  //商品详细信息 html
}
