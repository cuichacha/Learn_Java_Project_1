package com.tanhua.messages.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tanhua.commons.pojo.message.Users;
import com.tanhua.commons.service.huaxin.HuanXinService;
import com.tanhua.commons.service.message.MessageService;
import com.tanhua.commons.utils.TokenUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HuanXinService huanXinService;

    @Override
    public Boolean addContact(String token, Long userId) {
        Long loginUserId = TokenUtil.parseToken2Id(token);

        // 判断好友关系是否存在
        Query query1 = Query.query(Criteria.where("friendId").is(userId)
                .andOperator(Criteria.where("userId").is(loginUserId)));

        Query query2 = Query.query(Criteria.where("friendId").is(loginUserId)
                .andOperator(Criteria.where("userId").is(userId)));
        // 两轮查询
        Users users1 = mongoTemplate.findOne(query1, Users.class);
        Users users2 = mongoTemplate.findOne(query2, Users.class);
        if (users1 == null) {
            Users users = new Users();
            users.setId(ObjectId.get());
            users.setDate(System.currentTimeMillis());
            users.setUserId(loginUserId);
            users.setFriendId(userId);
            mongoTemplate.insert(users);
        }
        if (users2 == null) {
            Users users = new Users();
            users.setId(ObjectId.get());
            users.setDate(System.currentTimeMillis());
            users.setUserId(userId);
            users.setFriendId(loginUserId);
            mongoTemplate.insert(users);
        }
        Boolean result = huanXinService.addFriend(loginUserId, userId);
        if (result) {
            return users1 == null || users2 == null;
        } else {
            return false;
        }
    }
}
