package com.tanhua.videos;

import com.tanhua.commons.config.AliyunConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@MapperScan(basePackages = {"com.tanhua.videos.mapper"})
@ComponentScan(basePackages = {"com.tanhua.videos",
        "com.tanhua.commons.interceptor",
        "com.tanhua.commons.config"})
public class VideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }
}
