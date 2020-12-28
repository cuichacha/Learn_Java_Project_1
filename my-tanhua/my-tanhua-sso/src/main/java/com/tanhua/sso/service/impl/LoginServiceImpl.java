package com.tanhua.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.sso.mapper.UserInfoMapper;
import com.tanhua.sso.mapper.UserMapper;
import com.tanhua.sso.pojo.User;
import com.tanhua.sso.pojo.UserInfo;
import com.tanhua.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void generateVerificationCode() {
        String code = "123456";
        redisTemplate.opsForValue().set("code", code, 60L, TimeUnit.SECONDS);
    }

    @Override
    public boolean verifyCode(String verificationCode) {
        String code = redisTemplate.opsForValue().get("code");
        if (verificationCode.equals(code)) {
            // 验证码校验完成后需删除
            redisTemplate.delete("code");
            return true;
        }
        return false;
    }

    @Override
    public User findUserByPhone(String phone) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", phone);
        User user = userMapper.selectOne(wrapper);
//        User user = userMapper.findUserByPhone(phone);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void addUserInfo(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }
}
