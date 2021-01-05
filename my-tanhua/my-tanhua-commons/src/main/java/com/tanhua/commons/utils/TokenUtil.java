package com.tanhua.commons.utils;

import com.tanhua.commons.constants.RedisKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private TokenUtil() {
    }

//    @Value("${jwt.secret}")
//    private static String secret;

    public static String generateToken(String id, String phone) {
        String secret = "76bd425b6f29f7fcc2e0bfc286043df1";

        // 生成token
        Map<String, Object> header = new HashMap<String, Object>();
        header.put(JwsHeader.TYPE, JwsHeader.JWT_TYPE);
        header.put(JwsHeader.ALGORITHM, "HS256");

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", id);
        claims.put("mobile", phone);

        String token = Jwts.builder().setHeader(header).setClaims(claims).
                signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return token;
    }

    public static Map<String, Object> parseToken(String token) {
        String secret = "76bd425b6f29f7fcc2e0bfc286043df1";
        Map<String, Object> body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return body;
    }

    public static Long parseToken2Id(String token) {
        Map<String, Object> map = parseToken(token);
        String idStr = (String) map.get("id");
        long id = Long.parseLong(idStr);
        return id;
    }

    public static Boolean verifyToken(RedisTemplate<String, String> redisTemplate, String token) {
        Map<String, Object> map = parseToken(token);
        String id = (String) map.get("id");
        String mobile = (String) map.get("mobile");
        String idCache = RedisKey.ID_CACHE + id;
        String phoneCache = RedisKey.PHONE_CACHE + mobile;
        String redisID = redisTemplate.opsForValue().get(idCache);
        String redisMobile = redisTemplate.opsForValue().get(phoneCache);
        return id.equals(redisID) && mobile.equals(redisMobile);
    }
}
