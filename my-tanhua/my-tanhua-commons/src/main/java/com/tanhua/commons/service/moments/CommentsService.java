package com.tanhua.commons.service.moments;

import com.tanhua.commons.vo.moments.MovementsResult;
import org.bson.types.ObjectId;

public interface CommentsService {
    public abstract MovementsResult queryComments(String token, ObjectId publishId,
                                                  Integer startPage, Integer pageSize);

    public abstract Long queryCommentLike(ObjectId commentPublishId, Long userId);

    public abstract Boolean publishComment(String token, ObjectId movementPublishId, String content);

    public abstract Long likeComment(String toke, ObjectId publishId);

    public abstract Long disLikeComment(String toke, ObjectId publishId);

    public abstract void operateComment(String token, ObjectId publishId);

}
