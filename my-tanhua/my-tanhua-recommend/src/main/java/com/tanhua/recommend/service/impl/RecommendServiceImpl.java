package com.tanhua.recommend.service.impl;

import com.tanhua.commons.pojo.recommend.RecommendUser;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.service.recommend.RecommendService;
import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.commons.vo.TodayBest;
import com.tanhua.recommend.mapper.TodayBestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TodayBestMapper todayBestMapper;

    @Override
    public TodayBest findTodayBest(String token) {

        Map<String, Object> map = TokenUtil.parseToken(token);
        String idStr = (String) map.get("id");
        long id = Long.parseLong(idStr);
        Query query = Query.query(Criteria.where("toUserId").is(id))
                .with(Sort.by(Sort.Order.desc("score"))).limit(1);
        RecommendUser recommendUser = mongoTemplate.findOne(query, RecommendUser.class);

        if (recommendUser == null) {
            return null;
        }

        Long userId = recommendUser.getUserId();
        UserInfo userInfo = todayBestMapper.selectById(userId);
        TodayBest todayBest = new TodayBest();
        todayBest.setId(userInfo.getId());
        todayBest.setAge(userInfo.getAge());
        todayBest.setGender(userInfo.getSex().getValue() == 1 ? "男" : "女");
        double score = Math.floor(recommendUser.getScore());//取整,98.2 -> 98
        todayBest.setFateValue(Double.valueOf(score).longValue());
        todayBest.setNickname(userInfo.getNickName());
        todayBest.setAvatar(userInfo.getLogo());
        String[] tags = userInfo.getTags().split(",");
        todayBest.setTags(tags);
        return todayBest;
    }
}
