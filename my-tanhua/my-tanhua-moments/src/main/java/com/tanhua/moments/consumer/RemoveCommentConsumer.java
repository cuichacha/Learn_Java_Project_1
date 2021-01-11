package com.tanhua.moments.consumer;

import com.tanhua.commons.constants.RedisKey;
import com.tanhua.commons.pojo.moments.Comment;
import com.tanhua.commons.pojo.recommend.RecommendMoment;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "removeComment", consumerGroup = "remove-comment-consumer")
public class RemoveCommentConsumer implements RocketMQListener<Comment> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Comment comment) {
        // 从评论表移除数据
        Query query = Query.query(Criteria.where("_id").is(comment.getId()));
        mongoTemplate.remove(query);
        // 向动态推荐表插入数据，为Spark准备数据
        String publishId = comment.getPublishId().toHexString();
        Long userId = comment.getUserId();
        Integer commentType = comment.getCommentType();
        RecommendMoment recommendMoment = new RecommendMoment();
        recommendMoment.setId(ObjectId.get());
        String hashKey = RedisKey.PID_HASH;
        String pid = (String) redisTemplate.opsForHash().get(hashKey, publishId);
        if (pid != null) {
            recommendMoment.setPublishId(Long.valueOf(pid));
        }
        recommendMoment.setUserId(userId);
        recommendMoment.setDate(System.currentTimeMillis());
        Double score = 0.0;
        if (commentType == 1) {
            score -= 5;
        }
        if (commentType == 2) {
            score -= 10;
        }
        if (commentType == 3) {
            score -= 8;
        }
        recommendMoment.setScore(score);
        mongoTemplate.insert(recommendMoment);
    }
}
