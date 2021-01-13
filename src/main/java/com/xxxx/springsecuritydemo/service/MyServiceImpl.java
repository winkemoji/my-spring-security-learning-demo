package com.xxxx.springsecuritydemo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Permission;
import java.util.Collection;

@Service
public class MyServiceImpl implements MyService {
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        System.out.println("in has permission");
        //获取主体
        Object obj = authentication.getPrincipal();
        //判断主题是否属于UserDetails
        if(obj instanceof UserDetails){
            UserDetails userDetails = (UserDetails) obj;
            //获取权限
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            System.out.println("auth" + authorities);
            //判断请求的URI是否在权限里 request.getRequestURI() 返回值为请求路径/main.html
            return authorities.contains(new SimpleGrantedAuthority(request.getRequestURI()));


        }
        return false;
    }
}
