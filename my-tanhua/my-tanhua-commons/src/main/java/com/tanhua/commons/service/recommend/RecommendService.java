package com.tanhua.commons.service.recommend;

import com.tanhua.commons.pojo.recommend.RecommendUser;
import com.tanhua.commons.vo.TodayBest;

import java.util.List;

public interface RecommendService {
    public abstract List<RecommendUser> findRecommendUsersById(String token);

    public abstract TodayBest findTodayBest(String token);
}
