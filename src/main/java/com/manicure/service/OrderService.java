package com.manicure.service;

import java.util.Map;

import com.manicure.entity.TbOrder;
import com.manicure.entity.TbPayLog;

import entity.PageResult;

public interface OrderService {

	/**
	 * 添加订单
	 * @param order
	 */
	public Map add(TbOrder order);
	
	/**
	 * 根据用户ID获取支付日志
	 * @param userId
	 * @return
	 */
	public TbPayLog searchPayLogFromRedis(String userId);
	
	
	/**
	 * 支付成功修改状态
	 * @param out_trade_no
	 * @param transaction_id
	 */
	public void updateOrderStatus(String id,String userName);

	/**
	 * 根据id和useid查找订单和订单项封装map对象
	 * @param id
	 * @param username
	 * @return
	 */
	public Map findById(String id, String username);
	
	/**
	 * 用户分页查看订单列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize,String username,Integer time);

	/**
	 * 管理员查询订单发货
	 * @param pageNum
	 * @param pageSize
	 * @param tbOrder
	 * @return
	 */
	public PageResult search(int pageNum,int pageSize,TbOrder tbOrder);


	/**
	 * 管理员更新状态
	 * @param ids
	 */
	public void updateStatus(String[] ids,String status);

	
}
