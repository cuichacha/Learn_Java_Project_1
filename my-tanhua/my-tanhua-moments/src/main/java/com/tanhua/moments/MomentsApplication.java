package com.tanhua.moments;

import com.tanhua.commons.config.AliyunConfig;
import com.tanhua.commons.config.HuanXinConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@MapperScan(basePackages = {"com.tanhua.moments.mapper"})
@ComponentScan(basePackages = {"com.tanhua.moments",
        "com.tanhua.commons.interceptor",
        "com.tanhua.commons.config"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {HuanXinConfig.class})})
public class MomentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MomentsApplication.class, args);
    }
}
