package com.tanhua.commons.service.moments;

import com.tanhua.commons.pojo.moments.Comment;
import com.tanhua.commons.pojo.moments.Movements;
import com.tanhua.commons.pojo.moments.Publish;
import com.tanhua.commons.pojo.sso.UserInfo;
import com.tanhua.commons.vo.moments.MovementsResult;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

public interface MovementsService {
    public abstract MovementsResult queryFriendsMovements(String token, Integer startPage, Integer pageSize);

    public abstract MovementsResult queryRecommendedMovements(String token, Integer startPage, Integer pageSize);

    public abstract Long countComment(ObjectId publishId, Long userId, Integer commentType);

    public abstract Boolean publishMoment(String token, String textContent,
                                          String location, String latitude,
                                          String longitude, MultipartFile[] multipartFile);

    public abstract Long supportMovement(String token, ObjectId publishId, Integer commentType);

    public abstract Comment getComment(ObjectId publishId, Long userId, Integer commentType, String content);

    public abstract Long opposeMovement(String token, ObjectId publishId, Integer commentType);

    public abstract void operateComment(String token, ObjectId publishId, Integer commentType);

    public abstract Movements querySingleMovement(String token, ObjectId publishId);

    public abstract void fillPublishToMovements(Movements movements, Publish publish, Long userId);

    public abstract void fillUserInfoToMovements(Movements movements, UserInfo userInfo);
}
