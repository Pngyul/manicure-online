package com.manicure.service;

import com.manicure.entity.TbUser;

public interface UserService {

	/**
	 * 用户注册
	 * @param user
	 */
	public void add(TbUser user);
	
	/**
	 * 生成短信验证码
	 * @return
	 */
	public void createSmsCode(String phone);
	
	/**
	 * 判断短信验证码是否存在
	 * @param phone
	 * @return
	 */
	public boolean  checkSmsCode(String phone,String code);
	
	/**
	 * 回显用户基本信息
	 * @param username
	 * @return
	 */
	public TbUser getByUserName(String username);
	
	/**
	 * 修改用户信息
	 * @param user
	 */
	public void update(TbUser user);


	/**
	 * 修改密码
	 * @param user
	 */
    void updatePsw(TbUser user);
}
