package service;


import pojo.User;
import pojo.UserInfo;

import java.util.Map;

public interface LoginService {

    public abstract void generateVerificationCode(String phone);

    public abstract Map<String, Object> verifyCode(String verificationCode, String phone);

    public abstract User findUserByPhone(String phone);

    public abstract boolean isNewUser(String phone);

    void addUser(User user);

    void addUserInfo(UserInfo userInfo);
}
