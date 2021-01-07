package com.tanhua.moments.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.commons.pojo.moments.Comment;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RocketMQMessageListener(topic = "comment", consumerGroup = "comment-consumer")
public class CommentConsumer implements RocketMQListener<String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onMessage(String msg) {
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            int type = jsonNode.get("type").asInt();
            if (type == 1) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
