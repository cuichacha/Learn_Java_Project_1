package com.tanhua.sso.service.huanxin;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.config.HuanXinConfig;
import com.tanhua.commons.constants.RedisKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class HuanXinTokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HuanXinConfig huanXinConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getHuanXinToken() {
        String url = huanXinConfig.getUrl();
        String orgName = huanXinConfig.getOrgName();
        String appName = huanXinConfig.getAppName();
        String clientId = huanXinConfig.getClientId();
        String clientSecret = huanXinConfig.getClientSecret();

        String targetUrl = url + orgName + "/" + appName + "/token";
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "client_credentials");
        param.put("client_id", clientId);
        param.put("client_secret", clientSecret);

        String response = restTemplate.postForObject(targetUrl, param, String.class);
        Map parseObject = JSON.parseObject(response, Map.class);
        String access_token = (String) parseObject.get("access_token");
//        String application = (String) parseObject.get("application");
        Long expires_in = (Long) parseObject.get("expires_in");

        String redisKey = RedisKey.HUANXIN;
        redisTemplate.opsForValue().set(redisKey, access_token, (expires_in - 36000), TimeUnit.SECONDS);
        if (access_token != null) {
            return access_token;
        }
        return null;
    }

    public String takeHuanXinToken() {
        String redisKey = RedisKey.HUANXIN;
        String token = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(token) && StringUtils.isBlank(token)) {
            return getHuanXinToken();
        }
        return token;
    }
}
