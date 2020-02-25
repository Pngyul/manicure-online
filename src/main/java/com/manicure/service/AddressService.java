package com.manicure.service;

import java.util.List;

import com.manicure.entity.TbAddress;
import com.manicure.entity.TbAreas;
import com.manicure.entity.TbCities;
import com.manicure.entity.TbProvinces;

public interface AddressService {

	/**
	 * 返回所有省份
	 * @return
	 */
	public List<TbProvinces> findAll();
	
	/**
	 * 根据省id返回城市列表
	 * @param id
	 * @return
	 */
	public List<TbCities> findByProvincesId(String id);
	
	
	/**
	 * 返回地区列表
	 * @param id
	 * @return
	 */
	public List<TbAreas> findByCitiesId(String id);


	/**
	 * 添加地址
	 * @param address
	 */
	public void add(TbAddress address,String userName);

	/**
	 * 修改地址
	 * @param address
	 */
	public void update(TbAddress address);

	/**
	 * 删除
	 * @param id
	 */
	public void delete(Long id);

	/**
	 * 根据用户名查询所有地址
	 * @return
	 */
	public List<TbAddress> findAllAddress(String userName);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public TbAddress findById(Long id);

	/**
	 * 设置默认
	 * @param address
	 */
	public void isDefault(TbAddress address,String userName);

	
}
