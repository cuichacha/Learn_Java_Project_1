package com.tanhua.messages.service.huanxin;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.config.HuanXinConfig;
import com.tanhua.commons.constants.RedisKey;
import com.tanhua.commons.service.huaxin.HuanXinTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class HuanXinTokenServiceImpl implements HuanXinTokenService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HuanXinConfig huanXinConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getHuanXinToken() {
        // 拼接请求路径
        String url = huanXinConfig.getUrl();
        String orgName = huanXinConfig.getOrgName();
        String appName = huanXinConfig.getAppName();
        String clientId = huanXinConfig.getClientId();
        String clientSecret = huanXinConfig.getClientSecret();

        String targetUrl = url + orgName + "/" + appName + "/token";
        // 将必要信息放入请求体
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "client_credentials");
        param.put("client_id", clientId);
        param.put("client_secret", clientSecret);
        // 发送请求
        String response = restTemplate.postForObject(targetUrl, param, String.class);
        // 解析相应体，获取token和过期时间
        Map parseObject = JSON.parseObject(response, Map.class);
        String access_token = (String) parseObject.get("access_token");
//        String application = (String) parseObject.get("application");
        Long expires_in = (Long) parseObject.get("expires_in");
        // 将token存入Redis中
        String redisKey = RedisKey.HUANXIN;
        redisTemplate.opsForValue().set(redisKey, access_token, (expires_in - 36000), TimeUnit.SECONDS);
        return access_token;
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
