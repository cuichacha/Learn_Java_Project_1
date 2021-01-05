package com.tanhua.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.commons.pojo.recommend.QueryUser;
import com.tanhua.commons.pojo.recommend.RecommendUser;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.service.recommend.RecommendService;
import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.commons.vo.recommend.RecommendUsers;
import com.tanhua.commons.vo.recommend.TodayBest;
import com.tanhua.recommend.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public TodayBest findTodayBest(String token) {

        Long id = TokenUtil.parseToken2Id(token);
        Query query = Query.query(Criteria.where("toUserId").is(id))
                .with(Sort.by(Sort.Order.desc("score"))).limit(1);
        RecommendUser recommendUser = mongoTemplate.findOne(query, RecommendUser.class);

        if (recommendUser == null) {
            return null;
        }

        Long userId = recommendUser.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
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

    @Override
    public RecommendUsers findRecommendUsers(String token, QueryUser queryUser) {
        Long id = TokenUtil.parseToken2Id(token);

        Integer startPage = queryUser.getPage();
        Integer pageSize = queryUser.getPagesize();
        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);

        Query query = Query.query(Criteria.where("toUserId").is(id)).with(pageRequest)
                .with(Sort.by(Sort.Order.desc("score")));
        List<RecommendUser> recommendUserList = mongoTemplate.find(query, RecommendUser.class);

        RecommendUsers recommendUsers = new RecommendUsers();

//        List<UserInfo> users = new ArrayList<>();

        List<TodayBest> todayBests = new ArrayList<>();

        for (RecommendUser recommendUser : recommendUserList) {


            Double score = Math.floor(recommendUser.getScore());
            long value = score.longValue();

            TodayBest todayBest = new TodayBest();
            todayBest.setFateValue(value);

            String city = queryUser.getCity();
            String education = queryUser.getEducation();
            Integer age = queryUser.getAge();
            String genderStr = queryUser.getGender();
            String lastLogin = queryUser.getLastLogin();

            Long userId = recommendUser.getUserId();

            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();

            queryWrapper.in("user_id", userId);

            if (StringUtils.isNotEmpty(education) && StringUtils.isNotBlank(education)) {
//                queryWrapper.eq("edu", education);
            }

            if (age != null) {
//                queryWrapper.le("age", age);
            }

            if (StringUtils.isNotEmpty(genderStr) && StringUtils.isNotBlank(genderStr)) {
//                Integer gender = genderStr.equals("男") ? 1 : 2;
//                queryWrapper.eq("sex", gender);
            }

            if (StringUtils.isNotEmpty(city) && StringUtils.isNotBlank(city)) {
//                queryWrapper.like("city", city);
            }

            UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

            todayBest.setId(userInfo.getUserId());
            todayBest.setAge(userInfo.getAge());
            todayBest.setAvatar(userInfo.getLogo());
            todayBest.setGender(userInfo.getSex().getValue() == 1 ? "man" : "woman");
            todayBest.setNickname(userInfo.getNickName());
            todayBest.setTags(userInfo.getTags().split(","));
            todayBests.add(todayBest);
        }

        if (todayBests.isEmpty()) {
            return null;
        }

        recommendUsers.setPage(startPage);
        recommendUsers.setPagesize(pageSize);
        recommendUsers.setCounts(todayBests.size());
        recommendUsers.setItems(todayBests);

        return recommendUsers;
    }
}
