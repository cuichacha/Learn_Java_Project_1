package com.tanhua.videos.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.upload.FastImageFile;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.commons.constants.RedisKey;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.pojo.videos.Video;
import com.tanhua.commons.pojo.videos.Videos;
import com.tanhua.commons.service.moments.MovementsService;
import com.tanhua.commons.service.sso.PictureUploadService;
import com.tanhua.commons.service.videos.VideoService;
import com.tanhua.commons.utils.RedisKeyUtil;
import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.commons.vo.videos.VideoResult;
import com.tanhua.videos.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Reference
    private MovementsService movementsService;

    @Reference
    private PictureUploadService pictureUploadService;

    private String likeRedisKey;

    private String subscribeRedisKey;

    @Override
    public VideoResult queryVideoList(String token, Integer startPage, Integer pageSize) {
        Long loginUserId = TokenUtil.parseToken2Id(token);

        // 通过分页，查询视频数据
        PageRequest pageRequest = PageRequest.of(startPage - 1, pageSize);
        Query videoQuery = new Query().with(pageRequest);
        List<Video> videos = mongoTemplate.find(videoQuery, Video.class);
        // 通过视频数据中的userId，查到用户数据
        Set<Long> userIds = new HashSet<>();
        for (Video video : videos) {
            Long publishUserId = video.getUserId();
            userIds.add(publishUserId);
        }
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.in("user_id", userIds);
        List<UserInfo> userInfoList = userInfoMapper.selectList(wrapper);
        List<Videos> videosList = new ArrayList<>();
        for (Video video : videos) {
            for (UserInfo userInfo : userInfoList) {
                if (video.getUserId().equals(userInfo.getId())) {
                    Videos videoVO = new Videos();
                    videoVO.setId(video.getId().toHexString());
                    videoVO.setUserId(video.getUserId());
                    videoVO.setAvatar(userInfo.getLogo());
                    videoVO.setNickname(userInfo.getNickName());
                    videoVO.setCover(video.getPicUrl());
                    videoVO.setVideoUrl(video.getVideoUrl());
                    videoVO.setSignature("?????");
                    // 查询点赞数量
                    Long likeCount = movementsService.countComment(video.getId(), loginUserId, 1);
                    videoVO.setLikeCount(likeCount.intValue());
                    // 查询评论数量
                    Long commentCount = movementsService.countComment(video.getId(), loginUserId, 2);
                    videoVO.setCommentCount(commentCount.intValue());
                    // 判断是否已赞
                    likeRedisKey = RedisKeyUtil.generateCacheRedisKey(video.getId(), loginUserId, 1);
                    Boolean isLiked = redisTemplate.hasKey(likeRedisKey);
                    if (isLiked != null) {
                        videoVO.setHasLiked(isLiked ? 1 : 0);
                    } else {
                        videoVO.setHasLiked(0);
                    }
                    // 判断是否已关注
                    subscribeRedisKey = RedisKeyUtil.generateCacheRedisKey(loginUserId) + "_" + RedisKey.SUBSCRIBE;
                    Boolean isSubscribed = redisTemplate.hasKey(subscribeRedisKey);
                    if (isSubscribed != null) {
                        videoVO.setHasFocus(isSubscribed ? 1 : 0);
                    } else {
                        videoVO.setHasFocus(0);
                    }
                    videosList.add(videoVO);
                }
            }
        }
        VideoResult videoResult = new VideoResult();
        videoResult.setCounts(videosList.size());
        videoResult.setPage(startPage);
        videoResult.setPageSize(pageSize);
        videoResult.setPages(0);
        videoResult.setItems(videosList);
        return videoResult;
    }

    @Override
    public Boolean publishVideo(String token, MultipartFile thumbNail, MultipartFile videoFile) {
        Long userId = TokenUtil.parseToken2Id(token);

        String uploadThumbNail = pictureUploadService.upload(token, thumbNail);

        StorePath storePath = null;
        try {
            storePath = fastFileStorageClient.uploadFile(videoFile.getInputStream(),
                    videoFile.getSize(),
                    StringUtils.substringAfter(videoFile.getOriginalFilename(), "."),
                    null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Video video = new Video();
        video.setId(ObjectId.get());
        video.setUserId(userId);
        video.setPicUrl(uploadThumbNail);
        video.setVideoUrl(fdfsWebServer.getWebServerUrl() + "/" + storePath.getFullPath());
        video.setCreated(System.currentTimeMillis());
        video.setSeeType(1);
        Long vid = incrementVid(video.getId().toHexString());
        video.setVid(vid);
        mongoTemplate.insert(video);
        return true;
    }

    @Override
    public Boolean subscribe(String token, Long userId) {
        subscribeRedisKey = RedisKeyUtil.generateCacheRedisKey(userId) + "_" + RedisKey.SUBSCRIBE;
        redisTemplate.opsForValue().set(subscribeRedisKey, "subscribed");
        return true;
    }

    @Override
    public Boolean unsubscribe(String token, Long userId) {
        subscribeRedisKey = RedisKeyUtil.generateCacheRedisKey(userId) + "_" + RedisKey.SUBSCRIBE;
        redisTemplate.delete(subscribeRedisKey);
        return true;
    }

    @Override
    public Long incrementVid(String videoId) {
        String hashKey = RedisKey.VID_HASH;
        Boolean hasKey = redisTemplate.opsForHash().hasKey(hashKey, videoId);
        if (hasKey) {
            Object increment = redisTemplate.opsForHash().get(hashKey, videoId);
            if (increment != null) {
                return Long.valueOf(increment.toString());
            }
        }
        String redisKey = RedisKey.VIDEO_VID;
        Long increment = redisTemplate.opsForValue().increment(redisKey);
        if (increment!= null) {
            redisTemplate.opsForHash().put(hashKey, videoId, increment.toString());
            return increment;
        }
        return null;
    }


}
