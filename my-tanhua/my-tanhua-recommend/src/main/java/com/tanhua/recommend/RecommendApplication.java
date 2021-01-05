package com.tanhua.recommend;

import com.tanhua.commons.config.AliyunConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@MapperScan(basePackages = {"com.tanhua.recommend.mapper"})
@ComponentScan(basePackages = {"com.tanhua.recommend",
        "com.tanhua.commons.interceptor",
        "com.tanhua.commons.config"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {AliyunConfig.class})})
public class RecommendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecommendApplication.class, args);
    }
}
