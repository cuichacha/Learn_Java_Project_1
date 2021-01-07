package com.tanhua.moments.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.commons.constants.RedisKey;
import com.tanhua.commons.pojo.moments.Comment;
import com.tanhua.commons.pojo.moments.MovementComment;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.service.moments.CommentsService;
import com.tanhua.commons.service.moments.MovementsService;
import com.tanhua.commons.utils.RedisKeyUtil;
import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.commons.vo.moments.MovementsResult;
import com.tanhua.moments.mapper.UserInfoMapper;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MovementsService movementsService;

    // 查询评论列表
    @Override
    public MovementsResult queryComments(String token, ObjectId movementPublishId, Integer startPage, Integer pageSize) {
        // 根据参数构建查询条件
        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);
        Query query = Query.query(Criteria.where("publishId").is(movementPublishId)
                .andOperator(Criteria.where("commentType").is(2))).with(pageRequest);
        // 根据条件查询评论
        List<Comment> comments = mongoTemplate.find(query, Comment.class, "quanzi_comment");
        List<Long> userIds = new ArrayList<>();
        // 将评论中的userId放入集合
        for (Comment comment : comments) {
            Long userId = comment.getUserId();
            userIds.add(userId);
        }
        // 通过userId查询用户信息，放入集合
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        List<UserInfo> userInfoList = userInfoMapper.selectList(queryWrapper);
        List<MovementComment> movementCommentList = new ArrayList<>();
        // 将评论与用户信息进行组合
        for (Comment comment : comments) {
            for (UserInfo userInfo : userInfoList) {
                if (comment.getUserId().equals(userInfo.getUserId())) {
                    MovementComment movementComment = new MovementComment();
                    movementComment.setId(comment.getId().toHexString());
                    movementComment.setAvatar(userInfo.getLogo());
                    movementComment.setNickname(userInfo.getNickName());
                    movementComment.setContent(comment.getContent());
                    movementComment.setCreateDate(new DateTime(comment.getCreated()).toString("yyyy年MM月dd日 HH:mm"));

                    // 去Redis中，查询缓存的评论点赞数量
                    String commentLikeRedisKey = RedisKeyUtil.generateCacheRedisKey(movementPublishId, 1)
                            + "_" + RedisKey.COMMENT_LIKE_CACHE;
                    String commentLikeValue = redisTemplate.opsForValue().get(commentLikeRedisKey);

                    // 没有的话，去表中查询，查询完后，会存入缓存
                    if (commentLikeValue != null) {
                        movementComment.setLikeCount(Integer.valueOf(commentLikeValue));
                    } else {
                        Long commentLikes = queryCommentLike(movementPublishId);
                        movementComment.setLikeCount(commentLikes.intValue());
                    }

                    // 有无点赞
                    Boolean isLiked = redisTemplate.hasKey(commentLikeRedisKey);
                    if (isLiked != null) {
                        movementComment.setHasLiked(isLiked ? 1 : 0); //是否点赞（1是，0否）

                    }
                    movementCommentList.add(movementComment);
                }
            }
        }
        MovementsResult movementsResult = new MovementsResult();
        movementsResult.setCounts(movementCommentList.size());
        movementsResult.setPage(startPage);
        movementsResult.setPageSize(pageSize);
        movementsResult.setPages(0);
        movementsResult.setItems(movementCommentList);
        return movementsResult;
    }


    // 查询评论点赞数点赞
    @Override
    public Long queryCommentLike(ObjectId movementPublishId) {
        // 根据动态ID查询所有评论
        Query movementQuery = Query.query(Criteria.where("publishId").is(movementPublishId));
        List<Comment> comments = mongoTemplate.find(movementQuery, Comment.class, "quanzi_comment");
        List<ObjectId> commentLikeIds = new ArrayList<>();
        // 将所有评论的ID，存入集合
        for (Comment comment : comments) {
            ObjectId commentId = comment.getId();
            commentLikeIds.add(commentId);
        }
        // 将评论ID作为publishID，结合评论类型-->1（点赞），去查询（计算）到所有评论的点赞评论数量
        Query commentQuery = Query.query(Criteria.where("publishId").in(commentLikeIds));
        long commentLikes = mongoTemplate.count(commentQuery, Comment.class, "quanzi_comment");

        if (commentLikes != 0) {
            // 将评论点赞数量，存入Redis
            String commentLikeRedisKey = RedisKeyUtil.generateCacheRedisKey(movementPublishId, 1)
                    + "_" + RedisKey.COMMENT_LIKE_CACHE;
            redisTemplate.opsForValue().set(commentLikeRedisKey, String.valueOf(commentLikes), 60, TimeUnit.SECONDS);
            return commentLikes;
        }
        return null;
    }

    @Override
    public Boolean publishComment(String token, ObjectId movementPublishId, String content) {

        try {
            Comment comment = new Comment();
            comment.setId(ObjectId.get());
            comment.setCommentType(2);
            comment.setContent(content);
            comment.setParent(false);
            comment.setPublishId(movementPublishId);
            comment.setCreated(System.currentTimeMillis());
            comment.setUserId(TokenUtil.parseToken2Id(token));
            mongoTemplate.insert(comment, "quanzi_comment");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long likeComment(String toke, ObjectId publishId) {
        return movementsService.supportComment(toke, publishId, 1);
    }

    @Override
    public Long disLikeComment(String toke, ObjectId publishId) {
        return movementsService.opposeComment(toke, publishId, 1);
    }


}
