package com.tanhua.commons.config;

import com.tanhua.commons.interceptor.CacheInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CacheInterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired
    private CacheInterceptor cacheInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cacheInterceptor).addPathPatterns("/**");
    }
}
