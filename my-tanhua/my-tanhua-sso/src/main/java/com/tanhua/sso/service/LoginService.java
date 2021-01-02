package com.tanhua.sso.service;

import com.tanhua.sso.pojo.User;
import com.tanhua.sso.pojo.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface LoginService {

    public abstract void generateVerificationCode(String phone);

    public abstract Map<String, Object> verifyCode(String verificationCode, String phone);

    public abstract User findUserByPhone(String phone);

    public abstract boolean isNewUser(String phone);

    void addUser(User user);

    void addUserInfo(UserInfo userInfo);
}
