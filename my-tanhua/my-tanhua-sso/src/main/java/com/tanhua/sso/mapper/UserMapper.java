package com.tanhua.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pojo.User;

@Repository
public interface UserMapper extends BaseMapper<User> {

}
