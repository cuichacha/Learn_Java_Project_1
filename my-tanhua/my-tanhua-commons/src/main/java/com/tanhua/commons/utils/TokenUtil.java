package com.tanhua.commons.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private TokenUtil() {
    }

    public static String generateToken(String id, String phone, String secret) {
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

    public static Map<String, Object> parseToken(String token, String secret) {
        Map<String, Object> body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return body;
    }

    public static Boolean verifyToken(RedisTemplate<String, String> redisTemplate, String token, String secret) {
        Map<String, Object> map = parseToken(token, secret);
        String id = (String) map.get("id");
        String mobile = (String) map.get("mobile");
        String redisID = redisTemplate.opsForValue().get("id");
        String redisMobile = redisTemplate.opsForValue().get("mobile");
        if (id.equals(redisID) && mobile.equals(redisMobile)) {
            return true;
        } else {
            return false;
        }
    }
}
