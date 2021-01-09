package com.tanhua.moments.consumer;

import com.tanhua.commons.pojo.moments.Comment;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
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
        mongoTemplate.insert(comment, "quanzi_comment");
    }
}
