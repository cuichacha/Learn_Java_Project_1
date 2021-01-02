package com.tanhua.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.sso.mapper.UserInfoMapper;
import com.tanhua.sso.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pojo.User;
import pojo.UserInfo;
import service.LoginService;
import utils.TokenUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Value("${jwt.secret}")
    private String secret;

    private Map<String, Object> map = new LinkedHashMap<>();

    @Override
    public void generateVerificationCode(String phone) {
        // 使用正则表达式校验手机号
        Pattern pattern = Pattern.compile("^[1]([3-9])[0-9]{9}$");
        Matcher matcher = pattern.matcher(phone);
        boolean matches = matcher.matches();
        if (matches) {
            String code = "123456";
            String codeName = "sso_login_verificationCode_";
            codeName = codeName + phone;
            redisTemplate.opsForValue().set(codeName, code, 60L, TimeUnit.SECONDS);
        }
    }

    @Override
    public Map<String, Object> verifyCode(String verificationCode, String phone) {
        // 使用正则表达式校验验证码，包含非空判断
        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(verificationCode);
        boolean matches = matcher.matches();
        if (!matches) {
            return null;
        }
        String codeName = "sso_login_verificationCode_";
        codeName = codeName + phone;
        String code = redisTemplate.opsForValue().get(codeName);
        if (verificationCode.equals(code)) {
            // 验证码校验完成后需删除
            redisTemplate.delete(codeName);
            // 判断是否新用户
            boolean isNewUser = isNewUser(phone);

            if (isNewUser) {
                // 新用户，返回token, 返回Json字符串，要求填写资料，同时添加数据库信息
                User newUser = new User();
                newUser.setMobile(phone);
                addUser(newUser);
            }
            // 老用户，返回token，返回Json字符串，继续业务
            User user = findUserByPhone(phone);
            Long uid = user.getId();
            String id = String.valueOf(uid);
            String mobile = user.getMobile();
            String token = TokenUtil.generateToken(id, mobile, secret);
            map.put("token", token);
            if (isNewUser) {
                map.put("isNew", Boolean.valueOf("true"));
            } else {
                map.put("isNew", Boolean.valueOf("false"));
            }
            return map;
        }
        return null;
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
    public boolean isNewUser(String phone) {
        User user = findUserByPhone(phone);
        if (user != null) {
            return false;
        } else {
            return true;
        }
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
