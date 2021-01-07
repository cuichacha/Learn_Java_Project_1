package com.tanhua.moments.consumer;

import com.tanhua.commons.pojo.moments.Comment;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RocketMQMessageListener(topic = "comment", consumerGroup = "comment-consumer")
public class CommentConsumer implements RocketMQListener<Map<String, Object>> {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void onMessage(Map<String, Object> map) {
        Integer type = (Integer) map.get("type");
        if (type == 1) {
            Comment add = (Comment) map.get("add");
            mongoTemplate.insert(add, "quanzi_comment");
        }
        if (type == 2) {
            Query remove = (Query) map.get("remove");
            mongoTemplate.remove(remove, "quanzi_comment");
        }
    }
}
