package com.manicure.service.impl;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.manicure.entity.TbOrder;
import com.manicure.entity.TbOrderExample;
import com.manicure.entity.TbOrderExample.Criteria;
import com.manicure.entity.TbOrderItem;
import com.manicure.entity.TbOrderItemExample;
import com.manicure.entity.TbPayLog;
import com.manicure.mapper.TbOrderItemMapper;
import com.manicure.mapper.TbOrderMapper;
import com.manicure.service.OrderService;

import entity.Cart;
import entity.Orders;
import entity.PageResult;
import util.IdWorker;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private IdWorker idWorker;

	@Override
	public Map add(TbOrder order) {
		Map map=  new HashMap<>();
		// 得到购物车数据
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
		String orderId = idWorker.nextId()+"";
		
		order.setOrderId(orderId);// 订单ID
		order.setStatus("1");// 状态：未付款
		order.setCreateTime(new Date());// 订单创建日期
		order.setUpdateTime(new Date());// 订单更新日期
		// 循环购物车列表
		double money=0;
		Iterator<Cart> iterator = cartList.iterator();
		List<Cart> cartList1=new ArrayList<>();
		//判断是否被选中
		while(iterator.hasNext()){
			Cart cart = iterator.next();
			if(cart.getSeleted()){
				TbOrderItem orderItem = cart.getOrderItem();
				orderItem.setId(idWorker.nextId()+"");
				orderItem.setOrderId(orderId);// 订单ID
				money+=orderItem.getTotalFee().doubleValue();
				orderItemMapper.insert(orderItem);
				iterator.remove();
			}else{
				cartList1.add(cart);
				}
		 }
		
		order.setPayment(new BigDecimal(money));	
		orderMapper.insert(order);
		redisTemplate.boundHashOps("cartList").put(order.getUserId(), cartList1);
		map.put("orderId", orderId);
		map.put("money", money);
		return map;
	}

	@Override
	public TbPayLog searchPayLogFromRedis(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrderStatus(String id, String userName) {
		
			TbOrder order = findOne(id,userName);
			if(order!=null){
				order.setPaymentTime(new Date());
				order.setStatus("3");
				orderMapper.updateByPrimaryKey(order);
			}
		}

	@Override
	public Map findById(String id, String userName) {
		Map map = new HashMap<>();
		TbOrder order = findOne(id,userName);
		//防止修改url订单id查看别人的订单信息
		//判断order 是否为空 
		if(order == null){
			throw new RuntimeException();
		}
		TbOrderItemExample example = new TbOrderItemExample();
		example.createCriteria().andOrderIdEqualTo(id);
		List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(example );
		map.put("order", order);
		map.put("orderItemList", orderItemList);
		return map;
	}
	
	private TbOrder findOne(String id, String userName){
		TbOrderExample example = new TbOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andOrderIdEqualTo(id);
		if(userName!=null){
			criteria.andUserIdEqualTo(userName);
		}
		List<TbOrder> list = orderMapper.selectByExample(example );
		if(list.size()>0){
			return list.get(0);
	    }
		return null;
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize,String username,Integer time) {
		PageHelper.startPage(pageNum, pageSize);
		TbOrderExample example1  = new TbOrderExample();
		Criteria criteria = example1.createCriteria();
		criteria.andUserIdEqualTo(username);
		//-----时间帅选------
        if(time!=null){
            Calendar calendar = Calendar.getInstance();
            Date pre=null;
            Date post=new Date();
            if(time<366){
                calendar.setTime(new Date());
                if(time==30){
                    calendar.add(Calendar.DATE,-30);
                    pre=calendar.getTime();
                }else if(time==90){
                    calendar.add(Calendar.MONTH,-3);
                    pre=calendar.getTime();
                }else if(time==180){
                    calendar.add(Calendar.MONTH,-6);
                    pre=calendar.getTime();
                }else if(time==365){
                    calendar.set(calendar.get(Calendar.YEAR),0,1);
                    pre=calendar.getTime();
                }
            }else{
                if(time==2018){
                    calendar.set(2018,0,1);
                    pre = calendar.getTime();
                    calendar.add(Calendar.YEAR,1);
                    post = calendar.getTime();
                }else if(time==2017){
                    calendar.set(2017,0,1);
                    pre = calendar.getTime();
                    calendar.add(Calendar.YEAR,1);
                    post = calendar.getTime();
                }else if(time==2016){
                    calendar.set(2016,0,1);
                    pre = calendar.getTime();
                    calendar.add(Calendar.YEAR,1);
                    post = calendar.getTime();
                }
            }
            criteria.andCreateTimeBetween(pre,post);
        }
		//-----时间帅选------
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(example1);
		List<TbOrder> orderlist = page.getResult();
		List<Orders> ordersList = new ArrayList<>();
		for(TbOrder order : orderlist){
			TbOrderItemExample example = new TbOrderItemExample();
			example.createCriteria().andOrderIdEqualTo(order.getOrderId());
			List<TbOrderItem> list = orderItemMapper.selectByExample(example );
			Orders orders = new Orders(order,list);
			ordersList.add(orders);
		}
		return new PageResult(page.getTotal(), ordersList);
	}

	@Override
	public PageResult search(int pageNum, int pageSize, TbOrder tbOrder) {
		PageHelper.startPage(pageNum, pageSize);

		TbOrderExample example  = new TbOrderExample();
		Criteria criteria = example.createCriteria();
		if (tbOrder != null) {

			if (tbOrder.getOrderId() != null && tbOrder.getOrderId().length() > 0) {
				criteria.andOrderIdLike("%" + tbOrder.getOrderId() + "%");
			}else {
				if (tbOrder.getStatus()!= null && tbOrder.getStatus().length() > 0) {
					criteria.andStatusLike("%" + tbOrder.getStatus() + "%");
				}
				if (tbOrder.getUserId()!= null && tbOrder.getUserId().length() > 0) {
					criteria.andUserIdLike("%" + tbOrder.getUserId() + "%");
				}
			}
		}
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void updateStatus(String[] ids,String status) {
		for(String id:ids){
			TbOrder tbOrder = findOne(id,null);
			tbOrder.setStatus(status);
			if("4".equals(status)){
                tbOrder.setConsignTime(new Date());
            }
			orderMapper.updateByPrimaryKey(tbOrder);
		}

	}
}
