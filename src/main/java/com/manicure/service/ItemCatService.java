package com.manicure.service;

import java.util.List;

import com.manicure.entity.TbItem;
import com.manicure.entity.TbItemCat;

public interface ItemCatService {
	
	/**
	 * 根据父 id 查询实体 
	 * @param id
	 * @return
	 */
	public List findByParentId(Long id);

	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public TbItemCat findOne(Long id);

	/**
	 * 查找所有分类
	 * @return
	 */
	public List<TbItemCat> findAll();

}
