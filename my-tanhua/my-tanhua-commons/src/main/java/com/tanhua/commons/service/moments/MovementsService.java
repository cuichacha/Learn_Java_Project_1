package com.tanhua.commons.service.moments;

import com.tanhua.commons.pojo.moments.Comment;
import com.tanhua.commons.vo.moments.MovementsResult;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

public interface MovementsService {
    public abstract MovementsResult queryFriendsMovements(String token, Integer startPage, Integer pageSize);

    public abstract MovementsResult queryRecommendedMovements(String token, Integer startPage, Integer pageSize);

    public abstract Long countComment(ObjectId publishId, Integer commentType);

    public abstract Boolean publishMoment(String token, String textContent,
                                          String location, String latitude,
                                          String longitude, MultipartFile[] multipartFile);

    public abstract Long supportComment(String token, ObjectId publishId, Integer commentType);

    public abstract Comment getComment(ObjectId publishId, Long userId, Integer commentType, String content);

    public abstract Long opposeComment(String token, ObjectId publishId, Integer commentType);


}
