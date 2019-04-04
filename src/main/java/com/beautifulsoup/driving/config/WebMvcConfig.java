package com.beautifulsoup.driving.config;

import com.beautifulsoup.driving.filter.AclControlFilter;
import com.beautifulsoup.driving.filter.LoginFilter;
import com.beautifulsoup.driving.interceptor.UserInfoInterceptor;
import com.beautifulsoup.driving.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.io.Serializable;

//@ServletComponentScan(basePackages = "com.beautifulsoup.driving")
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private AgentRepository agentRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor()).addPathPatterns("/**");
    }


    @Bean
    public FilterRegistrationBean loginFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(loginFilter());
        registration.addUrlPatterns("/manage/*","/agent/add", "/agent/sendmail","/agent/update","/agent/get","/student/*");
        registration.setName("loginFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean manageFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(manageFilter());
        registration.addUrlPatterns("/manage/*","/student/*");
        registration.setName("manageFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public Filter loginFilter(){
        return new LoginFilter(stringRedisTemplate,redisTemplate,agentRepository);
    }

    @Bean
    public Filter manageFilter(){
        return new AclControlFilter();
    }

}
