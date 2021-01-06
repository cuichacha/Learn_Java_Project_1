package com.tanhua.moments.service.impl;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.commons.pojo.moments.*;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.service.moments.MovementsService;
import com.tanhua.commons.utils.RelativeDateFormat;
import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.commons.vo.moments.MovementsResult;
import com.tanhua.moments.mapper.UserInfoMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovementsServiceImpl implements MovementsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PictureUploadServiceImpl pictureUploadService;

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
        List<Publish> publishList = mongoTemplate.find(publishQuery, Publish.class, "quanzi_publish");

        List<Movements> movementsList = new ArrayList<>();

        // 组合查询到的数据

        for (Publish publish : publishList) {
            Movements movements = new Movements();
            movements.setId(publish.getId().toHexString());
//                    List<String> medias = publish.getMedias();
//                    String[] strings = new String[medias.size()];
//                    for (int i = 0; i < medias.size(); i++) {
//                        strings[i] = medias.get(i);
//                    }
//                    movements.setImageContent(strings);
            movements.setImageContent(publish.getMedias().toArray(new String[]{}));
            movements.setTextContent(publish.getText());
            movements.setUserId(publish.getUserId());
            movements.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
            movements.setCommentCount(10); //TODO 评论数
            movements.setDistance("1.2公里"); //TODO 距离
            movements.setHasLiked(1); //TODO 是否点赞（1是，0否）
            movements.setHasLoved(0); //TODO 是否喜欢（1是，0否）
            movements.setLikeCount(100); //TODO 点赞数
            movements.setLoveCount(80); //TODO 喜欢数
            movementsList.add(movements);
        }


        // 组合查询到的数据
        for (UserInfo userInfo : userInfos) {
            for (Movements movements : movementsList) {
                if (movements.getUserId().equals(userInfo.getUserId())) {
                    movements.setUserId(userInfo.getUserId());
                    movements.setAge(userInfo.getAge());
                    movements.setAvatar(userInfo.getLogo());
                    movements.setGender(userInfo.getSex().name().toLowerCase());
                    movements.setNickname(userInfo.getNickName());
                    movements.setTags(userInfo.getTags().split(","));
                }
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

        String publishTable = "quanzi_publish";

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
        // 先向发布表中插入一条数据
        try {
            mongoTemplate.insert(publish, publishTable);
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
        List<Users> users = mongoTemplate.find(query, Users.class, "tanhua_users");
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
}
