package com.manicure.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manicure.entity.TbGoods;
import com.manicure.entity.TbGoodsDesc;
import com.manicure.entity.TbUser;
import com.manicure.service.GoodsService;
import com.manicure.service.UserService;

import entity.Result;
import util.PhoneFormatCheckUtils;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@RequestMapping("/getUserName")
	public Map getUserName() {

		TbUser user = getUserInfo();
		Map map = new HashMap();
		map.put("userName", user.getUsername());
		map.put("headPic", user.getHeadPic());
		return map;
	}
	
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String smscode){
		
		//校验验证码是否正确
		boolean checkSmsCode = userService.checkSmsCode(user.getPhone(), smscode);
		if(!checkSmsCode){
			return new Result(false, "验证码不正确！");
		}
		try {
			userService.add(user);
			return new Result(true, "注册成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "注册失败");
		}
	}
	
	@RequestMapping("/sendCode")
	public Result sendCode(String phone){
		//判断手机号格式
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
			return new Result(false, "手机号格式不正确");
		}		
		try {
			userService.createSmsCode(phone);//生成验证码
			return new Result(true, "验证码发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "验证码发送失败");
		}		
	}
	
	@RequestMapping("/getUserInfo")
	public TbUser getUserInfo(){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			return userService.getByUserName(userName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new Result(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(true, "保存失败");
		}		
	}

	@RequestMapping("/updatePsw")
	public Result updatePsw(@RequestBody TbUser user){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		user.setUsername(userName);
		try {
			userService.updatePsw(user);
			return new Result(true, "修改密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改密码失败");
		}

	}

}
