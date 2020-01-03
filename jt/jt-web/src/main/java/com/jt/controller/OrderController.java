package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	
	private DubboOrderService orderService;
	@Reference
	private DubboCartService cartService;
	@RequestMapping("/create")
	public String create(Model model) {
		 //根据用户Id信息,获取全部购物车记录
		 Long userid = UserThreadLocal.get().getId();
		 List<Cart> cartList = cartService.findCartListByUserId(userid);
		 model.addAttribute("carts", cartList);
		 return "order-cart";
		 
	 }
	
	@RequestMapping("/submit")
	public  SysResult savaOrder(Order order) {
		//1.获取userId
		Long userId=UserThreadLocal.get().getId();
		order.setUserId(userId);
		
		//需要返回订单号orderId
		String orderId=orderService.saveOrder(order);
		return SysResult.success(orderId);
	}
}
