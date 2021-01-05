package com.tanhua.commons.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.commons.annotation.Cache;
import com.tanhua.commons.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CacheInterceptor implements HandlerInterceptor {

    @Value("${tanhua.cache.enable}")
    private Boolean enable;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!enable) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (!request.getMethod().equals("GET")) {
            return true;
        }

        if (!((HandlerMethod) handler).hasMethodAnnotation(Cache.class)) {
            return true;
        }

        String redisKey = RedisKeyUtil.generateCacheRedisKey(request);
        String redisValue = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(redisValue) && StringUtils.isBlank(redisValue)) {
            return true;
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(redisValue);

        return false;
    }
}
