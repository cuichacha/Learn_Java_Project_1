package com.tanhua.messages;

import com.tanhua.commons.config.AliyunConfig;
import com.tanhua.commons.config.HuanXinConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan(basePackages = {"com.tanhua.messages.mapper"})
@ComponentScan(basePackages = {"com.tanhua.messages",
        "com.tanhua.commons.interceptor",
        "com.tanhua.commons.config"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {AliyunConfig.class})})
public class MessageApplication {

    @Autowired
    private RestTemplateBuilder builder;

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
}
