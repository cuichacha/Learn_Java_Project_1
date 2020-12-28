package com.tanhua.sso.controller;

import com.alibaba.fastjson.JSON;
import com.tanhua.sso.enums.SexEnum;
import com.tanhua.sso.pojo.User;
import com.tanhua.sso.pojo.UserInfo;
import com.tanhua.sso.service.LoginService;
import com.tanhua.sso.vo.ErrorResult;
import com.tanhua.sso.vo.Result;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Value("${jwt.secret}")
    private String secret;

    @PostMapping("/login")
//    public void login(@RequestBody String parameter) {
    public void login(@RequestBody Map<String, String> param) {
        // 应该在此处校验手机号，但是接口文档没有写返回
        // 使用正则表达式校验手机号

        String phone = param.get("phone");
        Pattern pattern = Pattern.compile("^[1]([3-9])[0-9]{9}$");
        Matcher matcher = pattern.matcher(phone);
        boolean matches = matcher.matches();
        if (matches) {
            loginService.generateVerificationCode();
        }
    }

    @PostMapping("/loginVerification")
    public ResponseEntity<Result> loginVerification(@RequestBody Map<String, String> param) {
//        Map<String, String> param = JSON.parseObject(parameter, Map.class);
        String phone = param.get("phone");
        String verificationCode = param.get("verificationCode");
        boolean queryResult = loginService.verifyCode(verificationCode);

        Result result = new Result();

        if (!queryResult) {
            result.setStatusCode("002");
            result.setMessage("验证码错误！");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }

        // 生成token
        Map<String, Object> header = new HashMap<String, Object>();
        header.put(JwsHeader.TYPE, JwsHeader.JWT_TYPE);
        header.put(JwsHeader.ALGORITHM, "HS256");

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("mobile", phone);

        String token = Jwts.builder().setHeader(header).setClaims(claims).
                signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        User user = loginService.findUserByPhone(phone);
        Map<String, String> map = new HashMap<>();
        // 放入token信息
        map.put("token", token);

        String message = null;

        // 老用户，返回token，返回Json字符串，继续业务
        if (user != null) {
            map.put("isNew", "false");
            message = JSON.toJSONString(map);
            result.setStatusCode("200");
        } else {
            // 新用户，返回token, 返回Json字符串，要求填写资料，同时添加数据库信息
            User newUser = new User();
            newUser.setMobile(phone);
            loginService.addUser(newUser);
            // 向前台返回这个是新用户的信息
            map.put("isNew", "true");
            message = JSON.toJSONString(map);
        }

        result.setMessage(message);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/loginReginfo/head")
    public void saveAvatar() {

    }

    // 获取前台传过来的用户信息，
    @PostMapping("/loginReginfo")
    public void saveUserInfo(@RequestParam String Authorization, @RequestBody Map<String, String> param) {
//        Map<String, String> param = JSON.parseObject(parameter, Map.class);
        String gender = param.get("gender");
        String nickname = param.get("nickname");
        String birthday = param.get("birthday");
        String city = param.get("city");
        String header = param.get("header");

        UserInfo userInfo = new UserInfo();
        switch (gender) {
            case "1":
                userInfo.setSex(SexEnum.MAN);
            case "2":
                userInfo.setSex(SexEnum.WOMAN);
            case "3":
                userInfo.setSex(SexEnum.UNKNOWN);
        }
//        userInfo.setSex();
        userInfo.setNickName(nickname);
        userInfo.setBirthday(birthday);
        userInfo.setCity(city);
        userInfo.setLogo(header);
        String id = UUID.randomUUID().toString();
        long userId = Long.parseLong(id);
        userInfo.setUserId(userId);
        loginService.addUserInfo(userInfo);
    }
}

