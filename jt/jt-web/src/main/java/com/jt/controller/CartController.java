package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	
	@Reference(check = false)
	private DubboCartService cartService;
	/**
	 * 跳转到购物车
	 * @return
	 */
	@RequestMapping("/show")
	public String show(Model model) {
		Long userId=7L; //暂时固定,后期维护
		List<Cart> cartlist=cartService.findCartListByUserId(userId);
		return"cart";
	}
	/**
	 * 参数:
	 * @param cart
	 * @return
	 */
	@RequestMapping("/add")  //接受加入购物车的操作时的post请求发过来的图片,用cart对象接受,(json转对象需要pojo的set方法)  参数可用对象接受
	public String addCart(Cart cart) {
		Long userId=7L;
		cart.setUserId(userId);
		cartService.addCart(cart);
		return"redirect:/cart/show.html";//重定向列表页面
	}
	
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		
		Long userId=7L;
		cart.setId(userId);
		cartService.updateCartNum(cart);
		return SysResult.success();
		
	}
	/**
	 * 根据user_id和Item_id删除数据
	 * 删除数据,之后重定向到购物车展现页面
	 */
	@RequestMapping("/cart/{Item_id}")
	public String deleteCarts(Cart cart) {
		Long userId=7L;
		cart.setId(userId);
		cartService.deleteCarts(cart);
		return "redirect:/cart/show.html";
	}
}
