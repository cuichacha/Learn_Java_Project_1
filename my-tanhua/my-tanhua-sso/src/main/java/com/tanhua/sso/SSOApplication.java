package com.tanhua.sso;

import com.tanhua.commons.config.AliyunConfig;
import com.tanhua.commons.config.CacheInterceptorConfig;
import com.tanhua.commons.config.InterceptorConfig;
import com.tanhua.commons.interceptor.CacheInterceptor;
import com.tanhua.commons.interceptor.MyResponseBodyAdvice;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@MapperScan(basePackages = {"com.tanhua.sso.mapper"})
@ComponentScan(basePackages = {"com.tanhua.sso",
        "com.tanhua.commons.config",
        "com.tanhua.commons.interceptor"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {CacheInterceptorConfig.class, CacheInterceptor.class, MyResponseBodyAdvice.class})})
public class SSOApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class, args);
    }
}
