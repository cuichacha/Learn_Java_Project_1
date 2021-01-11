package com.tanhua.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.commons.constants.RedisKey;
import com.tanhua.commons.pojo.moments.*;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.service.moments.MovementsService;
import com.tanhua.commons.utils.RedisKeyUtil;
import com.tanhua.commons.utils.RelativeDateFormat;
import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.commons.vo.moments.MovementsResult;
import com.tanhua.moments.mapper.UserInfoMapper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class MovementsServiceImpl implements MovementsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PictureUploadServiceImpl pictureUploadService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private ObjectId objectId;

    private String likeRedisKey;

    private String loveRedisKey;

    @Override
    public MovementsResult queryFriendsMovements(String token, Integer startPage, Integer pageSize) {
        // 根据token获取用户ID，再根据ID查询时间线表
        Long userId = TokenUtil.parseToken2Id(token);
        String id = String.valueOf(userId);
        String tableName = "quanzi_time_line_" + id;
        // 进行分页处理，起始页需要减一
        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);
        // 构建查询条件
        Query timeLineQuery = new Query().with(Sort.by(Sort.Order.desc("date"))).with(pageRequest);
        // 封装时间线表的数据
        List<TimeLine> timeLineList = mongoTemplate.find(timeLineQuery, TimeLine.class, tableName);
        List<Long> idList = new ArrayList<>();
        List<ObjectId> publishIdList = new ArrayList<>();
        // 从时间线表里获取动态ID和用户ID添加到集合中
        for (TimeLine timeLine : timeLineList) {
            Long timeLineUserId = timeLine.getUserId();
            ObjectId publishId = timeLine.getPublishId();
            idList.add(timeLineUserId);
            publishIdList.add(publishId);
        }
        // 构建查询用户信息的条件
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", idList);
        List<UserInfo> userInfos = userInfoMapper.selectList(queryWrapper);
        // 构建查询动态信息的条件，从发布表中查询动态信息
        Query publishQuery = Query.query(Criteria.where("_id").in(publishIdList)).with(Sort.by(Sort.Order.desc("created")));
        List<Publish> publishList = mongoTemplate.find(publishQuery, Publish.class);

        List<Movements> movementsList = new ArrayList<>();


        // 组合查询到的数据
        for (Publish publish : publishList) {
            Movements movements = new Movements();
            fillPublishToMovements(movements, publish, userId);
            movementsList.add(movements);
        }


        // 组合查询到的数据
        for (UserInfo userInfo : userInfos) {
            for (Movements movements : movementsList) {
                fillUserInfoToMovements(movements, userInfo);
            }
        }

        if (movementsList.isEmpty()) {
            return null;
        }
        // 封装返回数据
        MovementsResult movementsResult = new MovementsResult();
        movementsResult.setCounts(movementsList.size());
        movementsResult.setPage(startPage);
        movementsResult.setPageSize(pageSize);
        movementsResult.setPages(0);
        movementsResult.setItems(movementsList);
        return movementsResult;
    }

    @Override
    public MovementsResult queryRecommendedMovements(String token, Integer startPage, Integer pageSize) {
        return null;
    }

    // 动态评论数统计
    @Override
    public Long countComment(ObjectId publishId, Long userId, Integer commentType) {
//        if (commentType == 1 || commentType == 2 || commentType == 3) {
        // 根据评论类型去查询真实数据
        Query query = Query.query(Criteria.where("publishId").is(publishId)
                .andOperator(Criteria.where("commentType").is(commentType)));

        // 将数据缓存进Redis
//            String redisKey = null;

        // 点赞类型，生成点赞类型的redisKey
//            if (commentType == 1) {
//                redisKey = RedisKeyUtil.generateCacheRedisKey(publishId, userId, commentType)
//                        + "_" + RedisKey.MOVEMENT_LIKE_CACHE;
//            }

        // 喜欢类型，生成喜欢类型的redisKey
//            if (commentType == 3) {
//                redisKey = RedisKeyUtil.generateCacheRedisKey(publishId, userId, commentType)
//                        + "_" + RedisKey.MOVEMENT_LOVE_CACHE;
//            }
//            if (redisKey != null) {
//                redisTemplate.opsForValue().set(redisKey, String.valueOf(result), 60, TimeUnit.SECONDS);
//            }
        return mongoTemplate.count(query, "quanzi_comment");
//        }
//        return 0L;
    }

    // 发布动态
    @Override
    public Boolean publishMoment(String token, String textContent, String location, String latitude, String longitude, MultipartFile[] multipartFile) {
        List<String> medias = new ArrayList<>();

        for (MultipartFile file : multipartFile) {
            // 先遍历图片，上传阿里云，获得图片地址数据，放入集合中
            String upload = pictureUploadService.upload(token, file);
            medias.add(upload);
        }

        // 通过token获取到用户ID
        Long userId = TokenUtil.parseToken2Id(token);
        String id = String.valueOf(userId);

        Publish publish = new Publish();
        publish.setUserId(userId);
        publish.setText(textContent);
        publish.setLocationName(location);
        publish.setLatitude(latitude);
        publish.setLongitude(longitude);
        publish.setSeeType(1);
        publish.setMedias(medias);
        publish.setId(ObjectId.get());
        publish.setCreated(System.currentTimeMillis());
        Long pid = incrementPid(publish.getId().toHexString());
        publish.setPid(pid);
        // 先向发布表中插入一条数据
        try {
            mongoTemplate.insert(publish);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // 向用户相册表中插入一条数据
        String albumTable = "quanzi_album_" + id;
        Album album = new Album();
        album.setPublishId(publish.getId());
        album.setId(ObjectId.get());
        album.setCreated(publish.getCreated());

        try {
            mongoTemplate.insert(album, albumTable);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 查询好友表，获得所有好友ID，向所有的好友时间线表中插入一条数据
        Query query = Query.query(Criteria.where("userId").is(userId));
        List<Users> users = mongoTemplate.find(query, Users.class);
        for (Users user : users) {
            Long userUserId = user.getUserId();
            String timeLineUserId = String.valueOf(userUserId);
            String timeLineTable = "quanzi_time_line_" + timeLineUserId;

            TimeLine timeLine = new TimeLine();
            timeLine.setPublishId(publish.getId());
            timeLine.setUserId(userUserId);
            timeLine.setId(ObjectId.get());
            timeLine.setDate(publish.getCreated());

            try {
                mongoTemplate.insert(timeLine, timeLineTable);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    // 点赞 / 喜欢
    @Override
    public Long supportMovement(String token, ObjectId publishId, Integer commentType) {

        operateComment(token, publishId, commentType);

        Long increment = null;

        if (commentType == 1) {
            increment = redisTemplate.opsForValue().increment(likeRedisKey);
        }
        if (commentType == 3) {
            increment = redisTemplate.opsForValue().increment(loveRedisKey);
        }

        // 开启一个新的线程，去进行MongoDB表的增加或者删除
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Comment comment = getComment(publishId, userId, commentType, null);
//                    mongoTemplate.insert(comment, "quanzi_comment");
//                }
//            }).start();

        // 通过MQ发送消息，进行MongoDB表的增加或者删除
        Comment comment = getComment(publishId, TokenUtil.parseToken2Id(token), commentType, null);
        objectId = comment.getId();
        rocketMQTemplate.convertAndSend("addComment", comment);

        return increment;
    }

    // 获取评论对象
    @Override
    public Comment getComment(ObjectId publishId, Long userId, Integer commentType, String content) {
        // 封装点赞 / 喜欢的对象
        Comment comment = new Comment();
        comment.setId(ObjectId.get());
        comment.setCommentType(commentType);
        comment.setCreated(System.currentTimeMillis());
        comment.setParent(false);
        comment.setPublishId(publishId);
        comment.setUserId(userId);
        comment.setContent(content);
        Query query = Query.query(Criteria.where("_id").is(publishId));
        Publish publish = mongoTemplate.findOne(query, Publish.class);
        if (publish != null) {
            Long publishUserId = publish.getUserId();
            comment.setPublishUserId(publishUserId);
        }
        return comment;
    }

    // 取消点赞 / 取消喜欢
    @Override
    public Long opposeMovement(String token, ObjectId publishId, Integer commentType) {

        operateComment(token, publishId, commentType);

        Long decrement = null;

        if (commentType == 1) {
            decrement = redisTemplate.opsForValue().decrement(likeRedisKey);
        }
        if (commentType == 3) {
            decrement = redisTemplate.opsForValue().decrement(loveRedisKey);
        }
        if (decrement == null || decrement <= 0) {
            decrement = 0L;
        }
        // 开启一个新的线程，去进行MongoDB表的增加或者删除
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Comment comment = getComment(publishId, userId, commentType, null);
//                    mongoTemplate.insert(comment, "quanzi_comment");
//                }
//            }).start();

        // 通过MQ发送消息，进行MongoDB表的增加或者删除
        Comment comment = getComment(publishId, TokenUtil.parseToken2Id(token), commentType, null);
        comment.setId(objectId);
        rocketMQTemplate.convertAndSend("removeComment", comment);

        return decrement;
    }

    @Override
    public void operateComment(String token, ObjectId publishId, Integer commentType) {

        // 根据token查询当前登录用户ID
        Long userId = TokenUtil.parseToken2Id(token);

        // 去Redis中查询数据
        if (commentType == 1) {
            String likeValue = redisTemplate.opsForValue().get(likeRedisKey);
            // 如果没查到数据，说明没有之前没有点赞
            if (likeValue == null) {
                // 获取已点赞数，写入Redis，返回数据
                Long likeNumber = countComment(publishId, userId, commentType);
                redisTemplate.opsForValue().set(likeRedisKey, String.valueOf(likeNumber));
//                return likeNumber;
//            } else {
                // 如果查到数据，直接转换类型返回结果
//                return Long.parseLong(likeValue);
            }
        }
        if (commentType == 3) {
            String loveValue = redisTemplate.opsForValue().get(loveRedisKey);
            // 如果没查到数据，说明没有之前没有点赞
            if (loveValue == null) {
                // 获取已点赞数，写入Redis，返回数据
                Long loveNumber = countComment(publishId, userId, commentType);
                redisTemplate.opsForValue().set(loveRedisKey, String.valueOf(loveNumber));
//                return loveNumber;
//            } else {
                // 如果查到数据，直接转换类型返回结果
//                return Long.parseLong(loveValue);
            }
        }
    }

    // 查询单条动态
    @Override
    public Movements querySingleMovement(String token, ObjectId publishId) {
        // 查询单个动态，便于后续的评论操作
        Movements movements = new Movements();
        Query publishQuery = Query.query(Criteria.where("_id").is(publishId));
        Publish publish = mongoTemplate.findOne(publishQuery, Publish.class);
        if (publish != null) {
            fillPublishToMovements(movements, publish, TokenUtil.parseToken2Id(token));

            Long userId = publish.getUserId();
            QueryWrapper<UserInfo> userInfoQuery = new QueryWrapper<>();
            userInfoQuery.eq("user_id", userId);
            UserInfo userInfo = userInfoMapper.selectOne(userInfoQuery);

            fillUserInfoToMovements(movements, userInfo);

            return movements;
        }
        return null;
    }

    @Override
    public void fillPublishToMovements(Movements movements, Publish publish, Long userId) {
        // 将Publish对象封装进Movement对象中
        movements.setId(publish.getId().toHexString());
        movements.setImageContent(publish.getMedias().toArray(new String[]{}));
        movements.setTextContent(publish.getText());
        movements.setUserId(publish.getUserId());
        movements.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));

        // 查询真实评论数
        Long commentNumber = countComment(publish.getId(), userId, 2);
        movements.setCommentCount(commentNumber.intValue());

        movements.setDistance("1.2公里"); //TODO 距离

        // 有无点赞
        likeRedisKey = RedisKeyUtil.generateCacheRedisKey(publish.getId(), userId, 1)
                + "_" + RedisKey.MOVEMENT_LIKE_CACHE;
        Boolean isLiked = redisTemplate.hasKey(likeRedisKey);
        if (isLiked != null) {
            movements.setHasLiked(isLiked ? 1 : 0); //是否点赞（1是，0否）
        }

        // 查询真实点赞数
        Long likeNumber = countComment(publish.getId(), userId, 1);
        movements.setLikeCount(likeNumber.intValue());

        // 有无喜欢
        loveRedisKey = RedisKeyUtil.generateCacheRedisKey(publish.getId(), userId, 3)
                + "_" + RedisKey.MOVEMENT_LOVE_CACHE;
        Boolean isLoved = redisTemplate.hasKey(loveRedisKey);
        if (isLoved != null) {
            movements.setHasLoved(isLoved ? 1 : 0); //是否点赞（1是，0否）
        }

        // 查询真实喜欢数
        Long loveNumber = countComment(publish.getId(), userId, 3);
        movements.setLoveCount(loveNumber.intValue());

    }

    @Override
    public void fillUserInfoToMovements(Movements movements, UserInfo userInfo) {
        // 将UserInfo对象封装进Movement对象中
        if (movements.getUserId().equals(userInfo.getUserId())) {
            movements.setUserId(userInfo.getUserId());
            movements.setAge(userInfo.getAge());
            movements.setAvatar(userInfo.getLogo());
            movements.setGender(userInfo.getSex().name().toLowerCase());
            movements.setNickname(userInfo.getNickName());
            movements.setTags(userInfo.getTags().split(","));
        }
    }

    @Override
    public Long incrementPid(String publishId) {
        String hashKey = RedisKey.PID_HASH;
        Boolean hasKey = redisTemplate.opsForHash().hasKey(hashKey, publishId);
        if (hasKey) {
            Object increment = redisTemplate.opsForHash().get(hashKey, publishId);
            if (increment != null) {
                return Long.valueOf(increment.toString());
            }
        }
        String redisKey = RedisKey.MOVEMENT_PID;
        Long increment = redisTemplate.opsForValue().increment(redisKey);
        if (increment!= null) {
            redisTemplate.opsForHash().put(hashKey, publishId, increment.toString());
            return increment;
        }
        return null;
    }


}
