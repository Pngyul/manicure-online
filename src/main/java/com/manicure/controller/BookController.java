package com.manicure.controller;

import com.manicure.entity.TbGoods;
import com.manicure.entity.TbService;
import com.manicure.service.BookService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookService bookService;

	@RequestMapping("/add")
	public Result book(@RequestBody TbService service ){

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		service.setuId(userName);
		try {
			bookService.add(service);
			return new Result(true,"预约成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"预约失败！");
		}
	}

	@RequestMapping("/getByUsername")
	public List<TbService> getByUsername(){

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return bookService.getByUsername(userName);
	}


	@RequestMapping("/search")
	public PageResult search(@RequestBody TbService service, int page, int rows  ){
		return bookService.findPage(service, page, rows);
	}


	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			bookService.updateStatus(ids,status);
			return new Result(true,"更新成功！");
		} catch (Exception e) {

			e.printStackTrace();
			return new Result(true,"更新失败！");
		}
	}

	@RequestMapping("/findOne")
	public TbService findOne(Long id){
		return bookService.finfOne(id);
	}
}
