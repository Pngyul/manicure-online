package com.manicure.service;

import java.util.List;

import entity.Cart;

public interface CartService {

	/**
	 * 添加到购物车
	 * @param cartList
	 * @param itemId
	 * @param num
	 * @return
	 */
	public List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);
	
	/**
	 * 在 redis 中查询购物车
	 * @param username
	 * @return
	 */
	public List<Cart> findCartListFromRedis(String username);
	
	/**
	 * 将购物车保存到 redis
	 * @param username
	 * @param cartList
	 */
	public void saveCartListToRedis(String username,List<Cart> cartList);

	/**
	 * 合并购物车
	 * @param cartList1
	 * @param cartList2
	 * @return
	 */
	public List<Cart> mergeCartList(List<Cart> cartList1,List<Cart> cartList2);
	
	/**
	 * 设置是否选中
	 * @param cartList
	 * @param itemId
	 * @param isSelected
	 * @return
	 */
	public List<Cart> updateSelection(List<Cart> cartList, Long itemId, String isSelected);
}
