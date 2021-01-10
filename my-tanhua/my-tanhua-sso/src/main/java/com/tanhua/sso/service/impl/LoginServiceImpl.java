package com.tanhua.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.commons.constants.RedisKey;
import com.tanhua.commons.enums.SexEnum;
import com.tanhua.commons.vo.sso.ErrorResult;
import com.tanhua.sso.mapper.UserInfoMapper;
import com.tanhua.sso.mapper.UserMapper;

import com.tanhua.sso.service.huanxin.HuanXinService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tanhua.commons.pojo.sso.User;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.service.sso.LoginService;
import com.tanhua.commons.utils.TokenUtil;

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

    @Autowired
    private HuanXinService huanXinService;

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
//            String codeName = "sso_login_verificationCode_";
            String codeName = RedisKey.LOGIN + phone;
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
//        String codeName = "sso_login_verificationCode_";
        String codeName = RedisKey.LOGIN + phone;
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
                // 对新用户进行环信的注册
                User user = findUserByPhone(phone);
                Long userId = user.getId();
                huanXinService.registerUser(userId);
            }
            // 老用户，返回token，返回Json字符串，继续业务
            User user = findUserByPhone(phone);
            Long uid = user.getId();
            String id = String.valueOf(uid);
            String mobile = user.getMobile();

            // 将用户的id和手机号，缓存进Redis，防止token校验频繁查询数据库
            String idCache = RedisKey.ID_CACHE + id;
            String phoneCache = RedisKey.PHONE_CACHE + phone;
            redisTemplate.opsForValue().set(idCache, id, 12L, TimeUnit.HOURS);
            redisTemplate.opsForValue().set(phoneCache, phone, 12L, TimeUnit.HOURS);

            String token = TokenUtil.generateToken(id, mobile);
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
    public ResponseEntity<Object> addUserAvatar(String token, String avatarUrl) {
        Map<String, Object> map = TokenUtil.parseToken(token);
        Object id = map.get("id");
        UserInfo userInfo = new UserInfo();
//        userInfo.setUserId(id);
        userInfo.setLogo(avatarUrl);
        userInfo.setCoverPic(avatarUrl);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", id);
        int result = userInfoMapper.update(userInfo, wrapper);

        if (result != 1) {
            ErrorResult errorResult = new ErrorResult();
            errorResult.setErrCode("005");
            errorResult.setErrMessage("用户头像存入失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }

        return ResponseEntity.ok(null);
    }

    @Override
    public void addUserInfo(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }


    @Override
    public Boolean saveUserInfo(String Authorization, Map<String, String> param) {
        // 先校验token，拦截器中已完成

        Map<String, Object> map = TokenUtil.parseToken(Authorization);
        String idStr = (String) map.get("id");
        long id = Long.parseLong(idStr);
        String gender = param.get("gender");
        String nickname = param.get("nickname");
        String birthday = param.get("birthday");
        String city = param.get("city");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(id);
        userInfo.setSex(StringUtils.equalsIgnoreCase(gender, "man") ? SexEnum.MAN : SexEnum.WOMAN);
        userInfo.setNickName(nickname);
        userInfo.setBirthday(birthday);
        userInfo.setCity(city);
        int result = userInfoMapper.insert(userInfo);
        return result == 1;
    }
}
