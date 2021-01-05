package com.tanhua.commons.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.commons.annotation.Cache;
import com.tanhua.commons.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {

    @Value("${tanhua.cache.enable}")
    private Boolean enable;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        boolean b1 = methodParameter.hasMethodAnnotation(GetMapping.class);
        boolean b2 = methodParameter.hasMethodAnnotation(Cache.class);

        return enable && b1 && b2;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter,
                                  MediaType mediaType, Class aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (o == null) {
            return null;
        }

        String redisValue = null;
        String redisKey = null;
        String timeStr = null;
        Long time = null;

        Cache cache = methodParameter.getMethodAnnotation(Cache.class);
        if (cache != null) {
            timeStr = cache.time();
            time = Long.parseLong(timeStr);
        } else {
            return o;
        }

        if (o instanceof String) {
            redisValue = (String) o;
        } else {
            try {
                redisValue = MAPPER.writeValueAsString(o);
                redisKey = RedisKeyUtil.generateCacheRedisKey(((ServletServerHttpRequest) serverHttpRequest).getServletRequest());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (redisKey != null && redisValue != null) {
            redisTemplate.opsForValue().set(redisKey, redisValue, time, TimeUnit.SECONDS);
        }

        return o;
    }
}
