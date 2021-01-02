package utils;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
}
