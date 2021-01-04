package com.tanhua.recommend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.commons.pojo.sso.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface TodayBestMapper extends BaseMapper<UserInfo> {
}
