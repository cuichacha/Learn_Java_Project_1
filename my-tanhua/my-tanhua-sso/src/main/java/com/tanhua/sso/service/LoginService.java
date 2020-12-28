package com.tanhua.sso.service;

import com.tanhua.sso.pojo.User;
import com.tanhua.sso.pojo.UserInfo;
import org.springframework.stereotype.Service;

public interface LoginService {

    public abstract void generateVerificationCode();

    public abstract boolean verifyCode(String verificationCode);

    public abstract User findUserByPhone(String phone);

    void addUser(User user);

    void addUserInfo(UserInfo userInfo);
}
