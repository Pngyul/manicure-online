package com.manicure.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.manicure.entity.TbOrder;
import com.manicure.service.OrderService;

import entity.PageResult;
import entity.Result;

@Controller
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;


	@RequestMapping("/add")
	public Map add(@RequestBody TbOrder order){
		
		//获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		order.setUserId(username);
		order.setSourceType("2");//订单来源  PC
		Map map = new HashMap<>();
		try {
			map = orderService.add(order);
			map.put("result", new Result(true, "提交成功"));
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", new Result(false, "提交订单失败"));
			return map;
		}
	}
	
	@RequestMapping("/update")
	public Result updateOrderStatus(String id){
		
		//获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			orderService.updateOrderStatus(id,username);
			return new Result(true, "支付成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "支付失败");
		}
	}
	
	@RequestMapping("/findById")
	public Map findById(String id){
		//获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map = null;
		try {
			map = orderService.findById(id,username);
			map.put("result", new Result(true, "查询成功！"));
			TbOrder order = (TbOrder) map.get("order");
		} catch (Exception e) {
			map = new HashMap<>();
			map.put("result", new Result(false, "你操作有误！"));
		}
		return map;
	}
	
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows,Integer time){
		//获取当前登录人账号
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return orderService.findPage(page, rows,username,time);
	}

	//-------------------管理员操作----------------------------

	@RequestMapping("/search")
	public PageResult  search(@RequestBody TbOrder tbOrder,int page,int rows){
		return orderService.search(page, rows,tbOrder);
	}

	@RequestMapping("/updateStatus")
	public Result  updateStatus(String[] ids,String status){
		try {
			orderService.updateStatus(ids, status);
			return new Result(true, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "更新失败");
		}
	}
	
}


