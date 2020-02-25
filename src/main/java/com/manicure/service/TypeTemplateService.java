package com.manicure.service;

import java.util.List;
import java.util.Map;

import com.manicure.entity.TbTypeTemplate;

public interface TypeTemplateService {
	
	/**
	 * 返回规格列表
	 * @return
	 */
	public List<Map> findSpecList(Long id);

	public List<TbTypeTemplate> findAll();
	
}
