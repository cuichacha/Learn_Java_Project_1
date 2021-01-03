package com.tanhua.sso.controller;


import com.tanhua.commons.enums.SexEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tanhua.commons.pojo.UserInfo;
import com.tanhua.commons.service.LoginService;
import com.tanhua.commons.vo.ErrorResult;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
//    public void login(@RequestBody String parameter) {
    public void login(@RequestBody Map<String, String> param) {
        // 应该在此处校验手机号，但是接口文档没有写返回
        String phone = param.get("phone");
        loginService.generateVerificationCode(phone);
    }

    @PostMapping("/loginVerification")
    public ResponseEntity<Object> loginVerification(@RequestBody Map<String, String> param) {
//        Map<String, String> param = JSON.parseObject(parameter, Map.class);
        String phone = param.get("phone");
        String verificationCode = param.get("verificationCode");

        Map<String, Object> query = loginService.verifyCode(verificationCode, phone);

//        Result result = new Result();
        ErrorResult errorResult = new ErrorResult();

        if (query == null) {
            errorResult.setErrCode("002");
            errorResult.setErrMessage("验证码错误！");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }

//        String result = JSON.toJSONString(query);
        return ResponseEntity.ok(query);
    }

    // 获取前台传过来的用户信息，
    @PostMapping("/loginReginfo")
    public void saveUserInfo(@RequestHeader String Authorization, @RequestBody Map<String, String> param) {
//        Map<String, String> param = JSON.parseObject(parameter, Map.class);

        loginService.saveUserInfo(Authorization, param);
    }
}

