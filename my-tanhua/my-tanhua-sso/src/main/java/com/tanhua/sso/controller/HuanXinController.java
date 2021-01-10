package com.tanhua.sso.controller;

import com.tanhua.commons.pojo.huaxin.HuanXinUser;
import com.tanhua.commons.utils.TokenUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/huanxin")
public class HuanXinController {

    @GetMapping("user")
    public ResponseEntity<HuanXinUser> queryHuanXinUser(@RequestHeader("Authorization") String token) {
        Long userId = TokenUtil.parseToken2Id(token);

        HuanXinUser huanXinUser = new HuanXinUser();
        huanXinUser.setUsername(userId.toString());
        huanXinUser.setPassword(DigestUtils.md5Hex(String.valueOf(userId)));

        return ResponseEntity.ok(huanXinUser);
    }
}
