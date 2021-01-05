package com.tanhua.recommend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.commons.annotation.Cache;
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

import java.util.*;

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
        // 先解析token，获取当前登录用户的ID
        Long id = TokenUtil.parseToken2Id(token);

        // 从参数中拿到分页相关参数
        Integer startPage = queryUser.getPage();
        Integer pageSize = queryUser.getPagesize();
        // 起始页码减一
        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);

        // 根据登录用户的ID，去MongoDB中，查询所有推荐给当前用户的用户，即推荐列表
        Query query = Query.query(Criteria.where("toUserId").is(id)).with(pageRequest)
                .with(Sort.by(Sort.Order.desc("score")));
        List<RecommendUser> recommendUserList = mongoTemplate.find(query, RecommendUser.class);

        // 拿到推荐列表中，所有用户的ID，组成ID集合
        Set<Long> idSet = new HashSet<>();
        for (RecommendUser recommendUser : recommendUserList) {
            Long userId = recommendUser.getUserId();
            idSet.add(userId);
        }

        // 从参数中，获取当前登录用户的筛选条件
        String city = queryUser.getCity();
        String education = queryUser.getEducation();
        Integer age = queryUser.getAge();
        String genderStr = queryUser.getGender();
        String lastLogin = queryUser.getLastLogin();

        // 根据筛选条件，去MySQL中，查询符合条件的用户
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.in("user_id", idSet);

        if (StringUtils.isNotEmpty(education) && StringUtils.isNotBlank(education)) {
//                queryWrapper.eq("edu", education);
        }

        if (age != null) {
//            queryWrapper.le("age", age);
        }

        if (StringUtils.isNotEmpty(genderStr) && StringUtils.isNotBlank(genderStr)) {
//                Integer gender = genderStr.equals("男") ? 1 : 2;
//                queryWrapper.eq("sex", gender);
        }

        if (StringUtils.isNotEmpty(city) && StringUtils.isNotBlank(city)) {
//                queryWrapper.like("city", city);
        }

        // 筛选出符合条件的新的推荐列表
        List<UserInfo> userInfos = userInfoMapper.selectList(queryWrapper);

        List<TodayBest> todayBests = new ArrayList<>();

        // 把符合条件的推荐列表中的用户信息，添加到返回值集合中
        for (UserInfo userInfo : userInfos) {
            // 但是UserInfo表中，没有缘分值相关数据，前台又需要这个数据，需要去MongoDB中查询
            Long userId = userInfo.getUserId();
            Query idQuery = Query.query(Criteria.where("userId").is(userId));
            RecommendUser recommendUser = mongoTemplate.findOne(idQuery, RecommendUser.class);
            Double score = Math.floor(recommendUser.getScore());
            long value = score.longValue();

            TodayBest todayBest = new TodayBest();
            // 对返回对象赋值，包括缘分值等，并添加到集合中
            todayBest.setFateValue(value);
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

        //按照缘分值进行倒序排序
        Collections.sort(todayBests, (o1, o2) -> new Long(o2.getFateValue() - o1.getFateValue()).intValue());

        RecommendUsers recommendUsers = new RecommendUsers();
        recommendUsers.setPage(startPage);
        recommendUsers.setPagesize(pageSize);
        recommendUsers.setCounts(todayBests.size());
        recommendUsers.setItems(todayBests);

        return recommendUsers;
    }
}
