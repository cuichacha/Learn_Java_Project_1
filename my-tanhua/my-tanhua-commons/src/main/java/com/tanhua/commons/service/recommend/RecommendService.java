package com.tanhua.commons.service.recommend;

import com.tanhua.commons.vo.recommend.TodayBest;

public interface RecommendService {

    public abstract TodayBest findTodayBest(String token);
}
