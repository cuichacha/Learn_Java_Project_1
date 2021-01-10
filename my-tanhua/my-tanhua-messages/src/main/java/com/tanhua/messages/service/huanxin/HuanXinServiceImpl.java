package com.tanhua.messages.service.huanxin;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.config.HuanXinConfig;
import com.tanhua.commons.pojo.huaxin.HuanXinUser;
import com.tanhua.commons.service.huaxin.HuanXinService;
import com.tanhua.commons.service.huaxin.HuanXinTokenService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class HuanXinServiceImpl implements HuanXinService {

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
//        HuanXinUser huanXinUser = new HuanXinUser(String.valueOf(userId), String.valueOf(userId));
        String body = JSON.toJSONString(Collections.singletonList(huanXinUser));
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        // 发送请求
        String response = restTemplate.postForObject(targetUrl, httpEntity, String.class);

        return response != null;
    }


    public Boolean addFriend(Long loginUserId, Long friendUserId) {
        // 拼接请求路径，从Redis中获取环信token
        String huanXinToken = huanXinTokenService.takeHuanXinToken();
        String url = huanXinConfig.getUrl();
        String orgName = huanXinConfig.getOrgName();
        String appName = huanXinConfig.getAppName();
        String loginUserName = String.valueOf(loginUserId);
        String friendUserName = String.valueOf(friendUserId);
        String targetUrl = url + orgName + "/" + appName + "/users/" + loginUserName + "/contacts/users/" + friendUserName;

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + huanXinToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        // 发送请求
        String response = restTemplate.postForObject(targetUrl, httpEntity, String.class);

        return response != null;
    }
}
