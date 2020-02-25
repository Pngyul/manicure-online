package com.manicure.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.manicure.entity.TbItem;
import com.manicure.entity.TbOrderItem;
import com.manicure.mapper.TbItemMapper;
import com.manicure.service.CartService;

import entity.Cart;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		
		//1.根据sku查找商品
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//判断购物列表是否存在该商品
		Cart cart = searchCartByItemId(cartList,itemId);
		if(cart!=null){
			//2.购物车列表中已存在该商品
			//2.1在原cart上添加数量，更改金额
			Integer newNum = cart.getOrderItem().getNum()+num;
			double price = cart.getOrderItem().getPrice().doubleValue();
			cart.getOrderItem().setNum(newNum);
			cart.getOrderItem().setTotalFee(new BigDecimal(newNum*price));
			if(newNum<=0){
				cartList.remove(cart);
			}
		}else{
			//3.购物车中不存在该商品
			//3.1创建新的cart对象
			cart = createOrderItem(item,num);
			cartList.add(cart);
		}
		//4.返回购物车列表
		return cartList;
	}
	
	/**
	 * 判断该商品是否存在购物列表
	 * @param cartList
	 * @param itemId
	 * @return
	 */
	private Cart searchCartByItemId(List<Cart> cartList,Long itemId){
		for(Cart cart1:cartList){
			if(itemId.equals(cart1.getOrderItem().getItemId())){
				return cart1;
			}
		}
		return null;
	}
	
	private Cart createOrderItem(TbItem item,Integer num){
		Cart cart = new Cart();
		cart.setSeleted(true);
		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPicPath(item.getImage());
		orderItem.setPrice(item.getPrice());
		orderItem.setTitle(item.getTitle());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
		cart.setOrderItem(orderItem);
		return cart;
	}

	@Autowired
	private RedisTemplate redisTemplate;
	@Override
	public List<Cart> findCartListFromRedis(String username) {
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
		if(cartList==null){
			cartList=new ArrayList();
		}
		return cartList;
	}
	@Override
	public void saveCartListToRedis(String username, List<Cart> cartList) {
		redisTemplate.boundHashOps("cartList").put(username, cartList);
	}

	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
		for(Cart cart: cartList2){
			cartList1= addGoodsToCartList(cartList1,cart.getOrderItem().getItemId(),cart.getOrderItem().getNum());		
		}		
		return cartList1;
	}

	@Override
	public List<Cart> updateSelection(List<Cart> cartList, Long itemId, String isSelected) {
		for(Cart cart2:cartList){
			if(cart2.getOrderItem().getItemId().equals(itemId)){
				if("1".equals(isSelected)){
					cart2.setSeleted(true);
				}else{
					cart2.setSeleted(false);
				}
				break;
			}
		}
		return cartList;
	}

}
