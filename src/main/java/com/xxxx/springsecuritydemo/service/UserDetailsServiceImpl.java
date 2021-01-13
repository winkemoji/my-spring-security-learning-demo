package com.xxxx.springsecuritydemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名去数据库查询，如果不存在抛出异常
        if(!"admin".equals(username)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //2.如果用户名存在，比较密码(注册时已经加密过) 如果匹配成功返回UserDetails
        String password = passwordEncoder.encode("123");

        //角色前面加ROLE,权限随便~ 看源码
        return new User(username,password, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN,admin,normal,/main.html,/insert,/delete"));

    }
}
