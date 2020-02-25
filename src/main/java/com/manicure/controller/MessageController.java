package com.manicure.controller;

import com.manicure.entity.TbMessage;
import com.manicure.service.MessageService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {
	
	@Autowired
	private MessageService messageService;

	@RequestMapping("/add")
	public Result add(@RequestBody TbMessage message){

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		message.setuId(userName);
		try {
			messageService.add(message);
			return new Result(true,"提交留言成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"提交留言失败！");
		}
	}


	@RequestMapping("/search")
	public PageResult search(@RequestBody TbMessage message, int page, int rows  ){
		return messageService.findPage(message, page, rows);
	}


	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			messageService.updateStatus(ids,status);
			return new Result(true,"更新成功！");
		} catch (Exception e) {

			e.printStackTrace();
			return new Result(false,"更新失败！");
		}
	}

	@RequestMapping("/dele")
	public Result dele(Long[] ids, String status){
		try {
			messageService.dele(ids);
			return new Result(true,"更新成功！");
		} catch (Exception e) {

			e.printStackTrace();
			return new Result(false,"更新失败！");
		}
	}

	@RequestMapping("/findOne")
	public TbMessage findOne(Long id){
		return messageService.findOne(id);
	}
}
