package com.manicure.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.manicure.entity.TbUser;
import com.manicure.entity.TbUserExample;
import com.manicure.mapper.TbUserMapper;
import com.manicure.service.UserService;

import util.SmsUtil;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Value("${template_code}")
	private String template_code;
	
	@Value("${sign_name}")
	private String sign_name;

	@Override
	public void add(TbUser user) {
		user.setCreated(new Date());//创建日期
		user.setUpdated(new Date());//修改日期
		user.setSex("2");
		//密码加密
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(user.getPassword());
		System.out.println("加密密码："+password);
		/*String password = DigestUtils.md5Hex(user.getPassword());//对密码加密*/
		user.setPassword(password);
		userMapper.insert(user);	
	}

	@Override
	public void createSmsCode(String phone){
		//生成6位随机数
		String code =  (long) (Math.random()*1000000)+"";
		System.out.println(phone+"验证码："+code);
		//存入缓存
		redisTemplate.boundHashOps("smscode").put(phone, code);
		//发送到用户手机	....
		Map m=new HashMap<>();
		m.put("code", code);
		try {
			SendSmsResponse response = SmsUtil.sendSms(phone,template_code,sign_name,JSON.toJSONString(m));					 
		    System.out.println("Code=" + response.getCode());
	        System.out.println("Message=" + response.getMessage());
	        System.out.println("RequestId=" + response.getRequestId());
	        System.out.println("BizId=" + response.getBizId());			
		} catch (ClientException e) {
			e.printStackTrace();			
		}		
		
	}

	@Override
	public boolean  checkSmsCode(String phone,String code){
		//得到缓存中存储的验证码
		String sysCode = (String) redisTemplate.boundHashOps("smscode").get(phone);
		if(sysCode==null){
			return false;
		}
		if(!sysCode.equals(code)){
			return false;
		}
		return true;		
	}

	@Override
	public TbUser getByUserName(String username) {
		TbUserExample example = new TbUserExample();
		example.createCriteria().andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example );
		return list.get(0);
	}

	@Override
	public void update(TbUser user) {
		user.setUpdated(new Date());//修改日期
		userMapper.updateByPrimaryKey(user);
	}

	@Override
	public void updatePsw(TbUser user) {
		TbUserExample example = new TbUserExample();
		example.createCriteria().andUsernameEqualTo(user.getUsername());
		List<TbUser> list = userMapper.selectByExample(example);
		TbUser user1 = null;
		if(list!=null){
			user1 = list.get(0);
		}
		//密码加密
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode(user.getPassword());
		user1.setPassword(password);
		user1.setUpdated(new Date());


		userMapper.updateByPrimaryKey(user1);
	}


}
