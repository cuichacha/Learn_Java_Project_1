package com.tanhua.messages.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.commons.pojo.messages.*;
import com.tanhua.commons.pojo.moments.Comment;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.service.huaxin.HuanXinService;
import com.tanhua.commons.service.messages.MessageService;
import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.commons.vo.messages.MessageResult;
import com.tanhua.messages.mapper.AnnouncementMapper;
import com.tanhua.messages.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HuanXinService huanXinService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AnnouncementMapper announcementMapper;

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
        // 环信加为好友
        Boolean result = huanXinService.addFriend(loginUserId, userId);
        if (result) {
            return users1 == null || users2 == null;
        } else {
            return false;
        }
    }

    @Override
    public MessageResult queryContactsLists(String token, Integer startPage, Integer pageSize, String keyword) {
        // 获取登录用户ID
        Long userId = TokenUtil.parseToken2Id(token);
        // 查询到登录用户所有好友的ID，并进行分页
        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);
        Query query = Query.query(Criteria.where("userId").is(userId)).with(pageRequest);
        List<Users> users = mongoTemplate.find(query, Users.class);
        List<Long> ids = new ArrayList<>();
        for (Users user : users) {
            Long friendId = user.getFriendId();
            ids.add(friendId);
        }
        // 如有条件，根据条件筛选
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.in("user_id", ids);
        if (StringUtils.isNotEmpty(keyword) && StringUtils.isBlank(keyword)) {
            wrapper.like("nick_name", keyword);
        }
        // 组合数据
        List<UserInfo> userInfoList = userInfoMapper.selectList(wrapper);
        List<Contacts> contacts = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            Contacts contact = new Contacts();
            contact.setAge(userInfo.getAge());
            contact.setAvatar(userInfo.getLogo());
            contact.setGender(userInfo.getSex().name().toLowerCase());
            contact.setNickname(userInfo.getNickName());
            contact.setUserId(String.valueOf(userInfo.getUserId()));
            contact.setCity(StringUtils.substringBefore(userInfo.getCity(), "-"));

            contacts.add(contact);
        }

        MessageResult messageResult = new MessageResult();
        messageResult.setCounts(contacts.size());
        messageResult.setPage(startPage);
        messageResult.setPagesize(pageSize);
        messageResult.setPage(0);
        messageResult.setItems(contacts);

        return messageResult;
    }

    @Override
    public MessageResult queryComments(String token, Integer startPage, Integer pageSize, Integer commentType) {
        // 解析登录用户ID
        Long userId = TokenUtil.parseToken2Id(token);
        // 构建查询条件，分页，倒序
        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);
        Query query = Query.query(Criteria.where("publishUserId").is(userId)
                .andOperator(Criteria.where("commentType").is(commentType)))
                .with(pageRequest).with(Sort.by(Sort.Order.desc("created")));
        List<Comment> comments = mongoTemplate.find(query, Comment.class);
        // 获取评论人的ID集合
        List<Long> ids = new ArrayList<>();
        for (Comment comment : comments) {
            Long commentUserId = comment.getUserId();
            ids.add(commentUserId);
        }
        // 查询userInfo集合
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", ids);
        List<UserInfo> userInfoList = userInfoMapper.selectList(queryWrapper);
        // 组合数据
        List<MessageComment> messageComments = new ArrayList<>();

        for (Comment comment : comments) {
            for (UserInfo userInfo : userInfoList) {
                if (comment.getUserId().longValue() == userInfo.getUserId().longValue()) {
                    MessageComment messageComment = new MessageComment();
                    messageComment.setId(comment.getId().toHexString());
                    messageComment.setAvatar(userInfo.getLogo());
                    messageComment.setNickname(userInfo.getNickName());
                    messageComment.setCreateDate(new DateTime(comment.getCreated()).toString("yyyy-MM-dd HH:mm"));

                    messageComments.add(messageComment);
                }
            }
        }

        MessageResult messageResult = new MessageResult();
        messageResult.setCounts(messageComments.size());
        messageResult.setPage(startPage);
        messageResult.setPagesize(pageSize);
        messageResult.setPage(0);
        messageResult.setItems(messageComments);

        return messageResult;
    }

    @Override
    public MessageResult queryAnnouncement(String token, Integer startPage, Integer pageSize) {
        // 查询公告表数据，MyBatisPlus分页，需要配置拦截器，具体内容看笔记
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created");
        IPage<Announcement> iPage = new Page<>(startPage, pageSize);
        IPage<Announcement> selectPage = announcementMapper.selectPage(iPage, queryWrapper);
        List<Announcement> announcementPage = selectPage.getRecords();
        List<MessageAnnouncement> messageAnnouncementList = new ArrayList<>();

        for (Announcement record : announcementPage) {
            MessageAnnouncement messageAnnouncement = new MessageAnnouncement();
            messageAnnouncement.setId(record.getId().toString());
            messageAnnouncement.setTitle(record.getTitle());
            messageAnnouncement.setDescription(record.getDescription());
            messageAnnouncement.setCreateDate(new DateTime(record.getCreated()).toString("yyyy-MM-dd HH:mm"));

            messageAnnouncementList.add(messageAnnouncement);
        }

        MessageResult messageResult = new MessageResult();
        messageResult.setCounts(messageAnnouncementList.size());
        messageResult.setPage(startPage);
        messageResult.setPagesize(pageSize);
        messageResult.setPage(0);
        messageResult.setItems(messageAnnouncementList);
        return messageResult;
    }



}
