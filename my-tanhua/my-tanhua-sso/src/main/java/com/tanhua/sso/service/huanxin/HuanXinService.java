package com.tanhua.sso.service.huanxin;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.config.HuanXinConfig;
import com.tanhua.commons.pojo.huaxin.HuanXinUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class HuanXinService {

    @Autowired
    private HuanXinTokenService huanXinTokenService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HuanXinConfig huanXinConfig;

    public Boolean registerUser(Long userId) {
        // 拼接请求路径，从Redis中获取环信token
        String huanXinToken = huanXinTokenService.takeHuanXinToken();
        String url = huanXinConfig.getUrl();
        String orgName = huanXinConfig.getOrgName();
        String appName = huanXinConfig.getAppName();
        String targetUrl = url + orgName + "/" + appName + "/users";

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + huanXinToken);

        // 注册用户放入请求体中
        HuanXinUser huanXinUser = new HuanXinUser(String.valueOf(userId), DigestUtils.md5Hex(String.valueOf(userId)));
        String body = JSON.toJSONString(Collections.singletonList(huanXinUser));
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // 发送请求
        String response = restTemplate.postForObject(targetUrl, httpEntity, String.class);

        return response != null;
    }


    public Boolean addFriend() {


        return null;
    }
}
