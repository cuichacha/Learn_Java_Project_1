package com.tanhua.commons.service.moments;

import com.tanhua.commons.vo.moments.MovementsResult;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

public interface MovementsService {
    public abstract MovementsResult queryFriendsMovements(String token, Integer startPage, Integer pageSize);

    public abstract MovementsResult queryRecommendedMovements(String token, Integer startPage, Integer pageSize);

    public abstract Long countComment(ObjectId publishId, Integer commentType, Long userId);

    public abstract Boolean publishMoment(String token, String textContent,
                                          String location, String latitude,
                                          String longitude, MultipartFile[] multipartFile);

    public abstract Long likeComment(String token, ObjectId publishId);
}
