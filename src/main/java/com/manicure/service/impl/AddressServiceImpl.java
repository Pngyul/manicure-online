package com.manicure.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manicure.entity.TbAddress;
import com.manicure.entity.TbAddressExample;
import com.manicure.entity.TbAreas;
import com.manicure.entity.TbAreasExample;
import com.manicure.entity.TbCities;
import com.manicure.entity.TbCitiesExample;
import com.manicure.entity.TbCitiesExample.Criteria;
import com.manicure.entity.TbProvinces;
import com.manicure.entity.TbProvincesExample;
import com.manicure.mapper.TbAddressMapper;
import com.manicure.mapper.TbAreasMapper;
import com.manicure.mapper.TbCitiesMapper;
import com.manicure.mapper.TbProvincesMapper;
import com.manicure.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private TbProvincesMapper provincesMapper;
	@Autowired
	private TbCitiesMapper citiesMapper;
	@Autowired
	private TbAreasMapper areasMapper;
	@Autowired
	private TbAddressMapper addressMapper;
	
	@Override
	public List<TbProvinces> findAll() {
		TbProvincesExample example  = new TbProvincesExample();
		List<TbProvinces> list = provincesMapper.selectByExample(example);
		return list;
	}

	@Override
	public List<TbCities> findByProvincesId(String id) {
		TbCitiesExample example = new TbCitiesExample();
		Criteria criteria = example.createCriteria();
		criteria.andProvinceidEqualTo(id);
		return citiesMapper.selectByExample(example );
	}

	@Override
	public List<TbAreas> findByCitiesId(String id) {
		TbAreasExample example = new TbAreasExample();
		example.createCriteria().andCityidEqualTo(id);
		return areasMapper.selectByExample(example );
	}

	@Override
	public void add(TbAddress address,String userName) {
		//封装地址
		String addressDetail = null;
		if(address.getProvinceId()!=null){
			addressDetail = setAddressDetail(address);
			
		}else{
			addressDetail=address.getAddress();
		}
		address.setAddress(addressDetail);
		address.setCreateDate(new Date());
		address.setIsDefault("0");
		address.setUserId(userName);
		addressMapper.insert(address);
	}

	@Override
	public void update(TbAddress address) {
		//封装地址
		String addressDetail = setAddressDetail(address);
		address.setAddress(addressDetail);
		addressMapper.updateByPrimaryKey(address);
	}

	@Override
	public void delete(Long id) {
		addressMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<TbAddress> findAllAddress(String userName) {
		TbAddressExample example  = new TbAddressExample();
		com.manicure.entity.TbAddressExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userName);
		return addressMapper.selectByExample(example);
		 
	}

	@Override
	public TbAddress findById(Long id) {
		return addressMapper.selectByPrimaryKey(id);
	}

	@Override
	public void isDefault(TbAddress address,String userName) {
		TbAddressExample example = new TbAddressExample();
		example.createCriteria().andUserIdEqualTo(userName);
		List<TbAddress> list = addressMapper.selectByExample(example );
		for(TbAddress addr : list){
			addr.setIsDefault("0");
			addressMapper.updateByPrimaryKey(addr);
		}
		addressMapper.updateByPrimaryKey(address);
	}
	
	private String setAddressDetail(TbAddress address){
		//省
		TbProvincesExample example  = new TbProvincesExample();
		example.createCriteria().andProvinceidEqualTo(address.getProvinceId());
		String strProvince = provincesMapper.selectByExample(example).get(0).getProvince();
		//城市
		TbCitiesExample example1 = new TbCitiesExample();
		example1.createCriteria().andCityidEqualTo(address.getCityId());
		String strCity = citiesMapper.selectByExample(example1).get(0).getCity();
		//乡镇
		TbAreasExample example2 = new TbAreasExample();
		example2.createCriteria().andAreaidEqualTo(address.getTownId());
		String strArea = areasMapper.selectByExample(example2).get(0).getArea();
		//封装地址
		String strAddress = strProvince+" "+strCity+" "+strArea+" "+address.getAddress();
		return strAddress;
	}


}
