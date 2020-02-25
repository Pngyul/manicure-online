package com.manicure.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.manicure.entity.TbItem;
import com.manicure.entity.TbItemCat;
import com.manicure.entity.TbItemCatExample;
import com.manicure.mapper.TbItemCatMapper;
import com.manicure.service.ItemCatService;


@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List findByParentId(Long id) {
		TbItemCatExample example = new TbItemCatExample();
		example.createCriteria().andParentIdEqualTo(id);
		return itemCatMapper.selectByExample(example );
		
		}

	@Override
	public TbItemCat findOne(Long id) {
		return itemCatMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<TbItemCat> findAll() {
		//每次执行查询的时候，一次性读取缓存进行存储 (因为每次增删改都要执行此方法)
		List<TbItemCat> list = itemCatMapper.selectByExample(null);
		/*for(TbItemCat itemCat:list){
			redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
		}
		System.out.println("更新缓存:商品分类表");*/
		return  list;
	}
}
