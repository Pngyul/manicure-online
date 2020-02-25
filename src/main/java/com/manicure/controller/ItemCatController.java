package com.manicure.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manicure.entity.TbItem;
import com.manicure.entity.TbItemCat;
import com.manicure.service.ItemCatService;


@RestController
@RequestMapping("/itemCat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/findByParentId")
	public List findByParentId(Long id){
		return itemCatService.findByParentId(id);
		
	}
	
	@RequestMapping("/findOne")
	public TbItemCat findOne(Long id){
		return itemCatService.findOne(id);
		
	}
	
	@RequestMapping("/findAll")
	public List<TbItemCat> findAll(){
		return itemCatService.findAll();
		
	}
	
	
	

}
