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
        Long userId = TokenUtil.parseToken2Id(token);
        String id = String.valueOf(userId);
        String tableName = "quanzi_time_line_" + id;

        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);
        Query timeLineQuery = new Query().with(Sort.by(Sort.Order.desc("date"))).with(pageRequest);
        List<TimeLine> timeLineList = mongoTemplate.find(timeLineQuery, TimeLine.class, tableName);
        List<Long> idList = new ArrayList<>();
        List<ObjectId> publishIdList = new ArrayList<>();

        for (TimeLine timeLine : timeLineList) {
            Long timeLineUserId = timeLine.getUserId();
            ObjectId publishId = timeLine.getPublishId();
            idList.add(timeLineUserId);
            publishIdList.add(publishId);
        }

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", idList);
        List<UserInfo> userInfos = userInfoMapper.selectList(queryWrapper);

        Query publishQuery = Query.query(Criteria.where("_id").in(publishIdList));
        List<Publish> publishList = mongoTemplate.find(publishQuery, Publish.class, "quanzi_publish");

        List<Movements> movementsList = new ArrayList<>();

        for (UserInfo userInfo : userInfos) {
            Movements movements = new Movements();
            movements.setUserId(userInfo.getUserId());
            movements.setAge(userInfo.getAge());
            movements.setAvatar(userInfo.getLogo());
            movements.setGender(userInfo.getSex().name().toLowerCase());
            movements.setNickname(userInfo.getNickName());
            movements.setTags(userInfo.getTags().split(","));
            movementsList.add(movements);
        }

        for (Movements movements : movementsList) {
            for (Publish publish : publishList) {
                if (movements.getUserId().equals(publish.getUserId())) {
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
                }
            }
        }
        if (movementsList.isEmpty()) {
            return null;
        }

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
            String upload = pictureUploadService.upload(token, file);
            medias.add(upload);
        }

        Long userId = TokenUtil.parseToken2Id(token);
        String id = String.valueOf(userId);
        String publishTable = "quanzi_time_line_" + id;

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

        try {
            mongoTemplate.insert(publish, publishTable);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

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
