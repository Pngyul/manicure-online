package com.manicure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.manicure.entity.TbUser;
import com.manicure.entity.TbUserExample;
import com.manicure.mapper.TbUserMapper;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private TbUserMapper usermapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("经过了UserDetailsServiceImpl");
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        TbUserExample example = new TbUserExample();
        example.createCriteria().andUsernameEqualTo(username);
		List<TbUser> list = usermapper.selectByExample(example );
		if(list.size()>0){
			return new User(username,list.get(0).getPassword(), grantedAuths);
		}
		else{
			return null;
			
		}
        
    }
}
