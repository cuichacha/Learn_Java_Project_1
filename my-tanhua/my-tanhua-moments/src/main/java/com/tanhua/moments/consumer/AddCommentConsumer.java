package com.tanhua.moments.consumer;

import com.tanhua.commons.pojo.moments.Comment;
import com.tanhua.commons.pojo.recommend.RecommendMoment;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "addComment", consumerGroup = "add-comment-consumer")
public class AddCommentConsumer implements RocketMQListener<Comment> {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void onMessage(Comment comment) {
        // 向评论表插入数据
        mongoTemplate.insert(comment);
        // 向动态推荐表插入数据，为Spark准备数据
        String publishId = comment.getPublishId().toHexString();
        Long userId = comment.getUserId();
        Integer commentType = comment.getCommentType();
        RecommendMoment recommendMoment = new RecommendMoment();
        recommendMoment.setId(ObjectId.get());
        recommendMoment.setPublishId(Long.valueOf(publishId));
        recommendMoment.setUserId(userId);
        recommendMoment.setDate(System.currentTimeMillis());
        Double score = 0.0;
        if (commentType == 1) {
            score += 5;
        }
        if (commentType == 2) {
            score += 10;
        }
        if (commentType == 3) {
            score += 8;
        }
        recommendMoment.setScore(score);
        mongoTemplate.insert(recommendMoment);
    }
}
