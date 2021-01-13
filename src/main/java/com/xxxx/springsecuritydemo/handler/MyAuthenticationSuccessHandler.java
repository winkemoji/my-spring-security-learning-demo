package com.xxxx.springsecuritydemo.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String forwardUrl;

    public MyAuthenticationSuccessHandler(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        System.out.println(httpServletRequest.getRemoteAddr());
        User user = (User)authentication.getPrincipal();
//        System.out.println(user.getUsername());
//        //输出null
//        System.out.println(user.getPassword());
//        System.out.println(user.getAuthorities());
        httpServletResponse.sendRedirect(forwardUrl);
    }
}
