package com.tanhua.commons.service;


import com.tanhua.commons.pojo.User;
import com.tanhua.commons.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface LoginService {

    public abstract void generateVerificationCode(String phone);

    public abstract Map<String, Object> verifyCode(String verificationCode, String phone);

    public abstract User findUserByPhone(String phone);

    public abstract boolean isNewUser(String phone);

    void addUser(User user);

    void addUserInfo(UserInfo userInfo);

    public abstract void saveUserInfo(@RequestParam String Authorization, @RequestBody Map<String, String> param);
}
