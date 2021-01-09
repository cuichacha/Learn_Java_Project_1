package com.tanhua.moments.consumer;

import com.tanhua.commons.pojo.moments.Comment;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "removeComment", consumerGroup = "remove-comment-consumer")
public class RemoveCommentConsumer implements RocketMQListener<Comment> {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void onMessage(Comment comment) {
        Query query = Query.query(Criteria.where("_id").is(comment.getId()));
        mongoTemplate.remove(query, "quanzi_comment");
    }
}
