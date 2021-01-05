package com.tanhua.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.commons.constants.RedisKey;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;

public class RedisKeyUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RedisKeyUtil() {
    }

    public static String generateCacheRedisKey(HttpServletRequest request) throws JsonProcessingException {
        StringBuffer requestURL = request.getRequestURL();
        String valueAsString = MAPPER.writeValueAsString(request.getParameterMap());
        String token = request.getHeader("Authorization");

        String data = requestURL + "_" + valueAsString + "_" + token;
        String name = DigestUtils.md5Hex(data);
        return RedisKey.SERVER_CACHE + name;
    }
}
