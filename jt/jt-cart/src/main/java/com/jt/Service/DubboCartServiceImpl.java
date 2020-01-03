package com.jt.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;

import javassist.expr.NewArray;

@Service
public class DubboCartServiceImpl implements DubboCartService{
	
	@Autowired
	private CartMapper cartMapper;
	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		List<Cart> cart = cartMapper.selectList(queryWrapper);
		return cart;
	}
	
	
	/**	
	 *	 购物车唯一标识:user_id和item_id
	 *	 用户重复购物不会生成新的购物信息，只会数量相加
	 *	1、根据userId和itemId查询数据库
	 *		true：数量相加   false ：新增操作
	 */
	@Override
	public void addCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		Cart careDB = cartMapper.selectOne(queryWrapper);
		if (careDB==null) {
			cart.setCreated(new Date())
			.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			int num=careDB.getNum()+cart.getNum();
			careDB.setNum(num)
			.setUpdated(new Date());
			//根据主键更新,其他的属性当做set条件
			cartMapper.updateById(careDB);
		}
	}


	@Override
	public void updateCartNum(Cart cart) {
		//需要修改的数据
		Cart cartTemp=new Cart();
		cartTemp.setNum(cart.getNum())
				.setUpdated(new Date());
		//条件选择器
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>();
		queryWrapper.eq("item_id", cart.getItemId())
					.eq("user_id", cart.getUserId());
		cartMapper.update(cartTemp, queryWrapper);
	}


	@Override
	public void deleteCarts(Cart cart) {
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
	}

}
