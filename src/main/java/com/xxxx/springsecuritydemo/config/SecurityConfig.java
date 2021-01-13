package com.xxxx.springsecuritydemo.config;

import com.xxxx.springsecuritydemo.handler.MyAccessDeniedHandler;
import com.xxxx.springsecuritydemo.handler.MyAuthenticationFailureHandler;
import com.xxxx.springsecuritydemo.handler.MyAuthenticationSuccessHandler;
import com.xxxx.springsecuritydemo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    //在application.properties 里面有注入
    @Autowired
    private DataSource dataSource;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //表单提交
        http.formLogin()
                //自定义登陆页面
                .loginPage("/showLogin")
                //自定义入参
//                .usernameParameter("username")
//                .passwordParameter("username")
                //必须和表单提交的接口一致，会去执行自定义登录逻辑
                .loginProcessingUrl("/login")
                //登录成功后跳转的页面,必须POST请求(前后端分离不能用这个东西)
                .successForwardUrl("/toMain")
                //自定义登录成功处理器
                //.successHandler(new MyAuthenticationSuccessHandler("/main.html"))
                //登录失败后跳转的页面,必须POST请求
                .failureForwardUrl("/toError");
                //自定义登录失败处理器
//                .failureHandler(new MyAuthenticationFailureHandler("/error.html"));
        //授权
        http.authorizeRequests()
                //放行login.html， 不需要认证(注意不要多加空格)
                .antMatchers("/showLogin").permitAll()
                //放行error.html， 不需要认证(注意不要多加空格)
                .antMatchers("/error.html").permitAll()
//                //放行静态资源
//                .antMatchers("/css/**","/js/**","/images/**").permitAll()
//                //正则放行静态资源
////                .regexMatchers(".+[.]png").permitAll()
//                //必须是POST方法请求予以放行
//                .regexMatchers(HttpMethod.POST,"/demo").permitAll()
//                //mvc匹配(需要properties 配置 servlet path. )
////                .mvcMatchers("/demo").servletPath("/xxxx").permitAll()
//                //权限控制(严格区分大小写) 或者hasRole 还有 hasAmyAuthority
//                .antMatchers("/main1.html").hasAuthority("ROLE_ADMIN")
//                //基于IP地址
////                .antMatchers("/main1.html").hasIpAddress("127.0.0.1")
//                //基于access
////                .antMatchers("/main1.html").access("hasRole('abc')")
//                //剩下所有的请求都必须认证才可以访问，必须登录(anyRequest 放到最后)
                .anyRequest().authenticated();
//                .anyRequest().access("@myServiceImpl.hasPermission(request,authentication)");

        //异常处理，自定义403页面
        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

        //记住我
        http.rememberMe()
                //设置数据源
                .tokenRepository(persistentTokenRepository)
                //超时时间设置
                .tokenValiditySeconds(120)
                //自定义登录逻辑
                .userDetailsService(userDetailsService);

        //退出登录
        http.logout()
                //退出成功后跳转的页面(默认不用做什么修改)
                .logoutSuccessUrl("/login.html");
//                .logoutUrl("user/logout");


        //关掉csrf防护
        /*
        * csrf为了保证不是其他第三方网站访问，要求请求时携带参数名为
        * _csrf值为token,如果token和服务端的token匹配成功，则正常访问。
        * */
//        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        //自动建表，第一次启动时开始，第二次启动注释掉
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
}
