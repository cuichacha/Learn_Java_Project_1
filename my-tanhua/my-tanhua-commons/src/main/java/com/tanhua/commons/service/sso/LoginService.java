package com.tanhua.commons.service.sso;


import com.tanhua.commons.pojo.sso.User;
import com.tanhua.commons.pojo.sso.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface LoginService {

    public abstract void generateVerificationCode(String phone);

    public abstract Map<String, Object> verifyCode(String verificationCode, String phone);

    public abstract User findUserByPhone(String phone);

    public abstract boolean isNewUser(String phone);

    void addUser(User user);

    public abstract ResponseEntity<Object> addUserAvatar(String token, String avatarUrl);

    void addUserInfo(UserInfo userInfo);

    public abstract Boolean saveUserInfo(@RequestParam String Authorization, @RequestBody Map<String, String> param);
}
