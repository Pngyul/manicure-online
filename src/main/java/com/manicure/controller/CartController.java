package com.manicure.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.manicure.service.CartService;

import entity.Cart;
import entity.Result;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	@RequestMapping("/findCartList")
	public List<Cart> findCartList() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		String cartListString = util.CookieUtil.getCookieValue(request, "cartList", "UTF-8");
		if (cartListString == null || cartListString.equals("")) {
			cartListString = "[]";
		}
		List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
		if(userName.equals("anonymousUser")){//如果未登录
			//在本地 Cookie 获取购物列表
			return cartList_cookie;
		}else{
			//如果已登录		
			List<Cart> cartList_redis =cartService.findCartListFromRedis(userName);//从redis中提取
			if(cartList_cookie.size()>0){//如果本地存在购物车
				//合并购物车
				cartList_redis=cartService.mergeCartList(cartList_redis, cartList_cookie);	
				//清除本地cookie的数据
				util.CookieUtil.deleteCookie(request, response, "cartList");
				//将合并后的数据存入redis 
				cartService.saveCartListToRedis(userName, cartList_redis); 
			}			
			return cartList_redis;
		}			
	}

	@RequestMapping("/addGoodsToCartList")
	public Result addGoodsToCartList(Long itemId,Integer num){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName(); 
		try {			
			List<Cart> cartList =findCartList();//获取购物车列表
			cartList = cartService.addGoodsToCartList(cartList, itemId, num);
			if(userName.equals("anonymousUser")){ //如果是未登录，保存到cookie
				util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList),3600*24,"UTF-8");
			}else{//如果是已登录，保存到redis
				cartService.saveCartListToRedis(userName, cartList);			
			}
			return new Result(true, "添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败");
		}
	}	
	
	
	@RequestMapping("/updateSelection")
	public Result updateSelection(Long itemId,String isSelected){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName(); 
		try {			
			List<Cart> cartList =findCartList();//获取购物车列表
			cartList = cartService.updateSelection(cartList, itemId, isSelected);
			if(userName.equals("anonymousUser")){ //如果是未登录，保存到cookie
				util.CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList),3600*24,"UTF-8");
			}else{//如果是已登录，保存到redis
				cartService.saveCartListToRedis(userName, cartList);			
			}
			return new Result(true, "设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "设置失败");
		}
	}
	
	

	
}


