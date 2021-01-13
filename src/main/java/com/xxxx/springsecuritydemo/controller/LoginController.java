package com.xxxx.springsecuritydemo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
//    //判断ROLE
//    @Secured("ROLE_ADMIN")
    //PreAuthorize允许角色以ROLE_开头，也可以不以ROLE_开头
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/toMain")
    public String main(){
        System.out.println("i am toMain");
        return "redirect:main.html";
    }

    @RequestMapping("/toError")
    public String error(){
        return "redirect:error.html";
    }

    @RequestMapping("/demo")
    //ResponseBody 返回json
//    @ResponseBody
    public String demo(){
        return "demo";
    }



    @GetMapping("/showLogin")
    public String showLogin(){
        return "login";
    }
}
