package com.manicure.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.manicure.entity.TbSpecificationOption;
import com.manicure.entity.TbSpecificationOptionExample;
import com.manicure.entity.TbTypeTemplate;
import com.manicure.mapper.TbSpecificationOptionMapper;
import com.manicure.mapper.TbTypeTemplateMapper;
import com.manicure.service.TypeTemplateService;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService{
	
	@Autowired
	private TbTypeTemplateMapper typeTemplateMapper;
	
	@Autowired
	private TbSpecificationOptionMapper specificationOption;
	
	@Autowired
	private RedisTemplate redisTemplate;
	

	@Override
	public List<Map> findSpecList(Long id) {
		
		//1.根据模板id返回规格对象
		TbTypeTemplate template = typeTemplateMapper.selectByPrimaryKey(id);
		List<Map> list = JSON.parseArray(template.getSpecIds(), Map.class);
		//2.循环规格List，添加规格选项
		for(Map map:list){
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			example.createCriteria().andSpecIdEqualTo(new Long((Integer)map.get("id")));
			List<TbSpecificationOption> options = specificationOption.selectByExample(example);
			map.put("options", options);
		}
		//3.返回list
		
		
		/*-----------暂时将数据存入缓存-------------------*/
		/*saveToRedis();
		System.out.println("存储功效列表");*/
		/*-----------暂时将数据存入缓存-------------------*/
		return list;
	}
	
	/**
	 * 将数据存入缓存
	 */
	private void saveToRedis(){
		//获取模板数据
		List<TbTypeTemplate> typeTemplateList = findAll();
		//循环模板
		for(TbTypeTemplate typeTemplate :typeTemplateList){				
			//存储功效列表		
			List<Map> efficiencyList = JSON.parseArray(typeTemplate.getEfficiencyIds(), Map.class);			
			redisTemplate.boundHashOps("efficiencyList").put(typeTemplate.getId(), efficiencyList);
		}
		
	}

	@Override
	public List<TbTypeTemplate> findAll() {
		return typeTemplateMapper.selectByExample(null);
	}

}
