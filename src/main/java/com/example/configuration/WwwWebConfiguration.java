package com.example.configuration;

import com.example.interceptor.LoginRequiredInterceptor;
import com.example.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Component
public class WwwWebConfiguration extends WebMvcConfigurerAdapter {
   @Autowired
    PassportInterceptor passportInterceptor;
   @Autowired
    LoginRequiredInterceptor requiredInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(requiredInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
