package com.tanhua.commons.service.recommend;

import com.tanhua.commons.pojo.recommend.QueryUser;
import com.tanhua.commons.vo.recommend.RecommendUsers;
import com.tanhua.commons.vo.recommend.TodayBest;

public interface RecommendService {

    public abstract TodayBest findTodayBest(String token);

    public abstract RecommendUsers findRecommendUsers(String token, QueryUser queryUser);
}
