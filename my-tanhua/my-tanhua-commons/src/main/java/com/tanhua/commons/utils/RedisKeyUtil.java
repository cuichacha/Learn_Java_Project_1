package com.tanhua.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.commons.constants.RedisKey;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;

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

    public static String generateCacheRedisKey(ObjectId publishId, Long userId, Integer commentType) {
        String str1 = publishId.toString();
        String str2 = String.valueOf(userId);
        String str3 = String.valueOf(commentType);
        String redisKey = DigestUtils.md5Hex((str1 + "_" + str2 + "_" + str3));
        return redisKey;
    }
}
