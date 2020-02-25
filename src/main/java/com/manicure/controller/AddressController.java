package com.manicure.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manicure.entity.TbAddress;
import com.manicure.entity.TbAreas;
import com.manicure.entity.TbCities;
import com.manicure.entity.TbProvinces;
import com.manicure.service.AddressService;

import entity.Result;

@RestController
@RequestMapping("/address")
public class AddressController {
	
	@Autowired
	private AddressService addressService;
	
	@RequestMapping("/findAll")
	public List<TbProvinces> findAll1(){
		return addressService.findAll();
	}
	
	@RequestMapping("/findByProvenceId")
	public List<TbCities> findByProvenceId(String parentId){
		return addressService.findByProvincesId(parentId);
	}
	
	@RequestMapping("/findByCityId")
	public List<TbAreas> findByCityId(String parentId){
		return addressService.findByCitiesId(parentId);
	}
	
	@RequestMapping("/findAllAddress")
	public List<TbAddress> findAllAddress(){
			String userName = SecurityContextHolder.getContext().getAuthentication().getName();
			return addressService.findAllAddress(userName);
	}
	
	@RequestMapping("/add")
	public Result add(@RequestBody TbAddress address){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			addressService.add(address,userName);
			return new Result(true, "添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "添加失败！");
		}
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody TbAddress address){
		try {
			addressService.update(address);
			return new Result(true, "修改成功！");
		} catch (Exception e) {
			return new Result(false, "修改失败！");
		}
	}
	
	@RequestMapping("/delete")
	public Result delete(Long id){
		try {
			addressService.delete(id);
			return new Result(true, "删除成功！");
		} catch (Exception e) {
			return new Result(false, "删除失败！");
		}
	}
	
	@RequestMapping("/isDefault")
	public Result isDefault(@RequestBody TbAddress address){
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		address.setIsDefault("1");
		try {
			addressService.isDefault(address,userName);
			return new Result(true, "设置成功！");
		} catch (Exception e) {
			return new Result(true, "设置失败！");
		}
	}
	
	@RequestMapping("/findById")
	public TbAddress findById(Long id){
		return addressService.findById(id);
	}

}
