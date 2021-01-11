package com.tanhua.commons.pojo.videos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "recommend_video")
public class RecommendVideo implements Serializable {
    private static final long serialVersionUID = -3136732836884934873L;

    private ObjectId id;
    private Long userId;// 用户id
    private Long videoId; //视频id，需要转化为Long类型
    private Double score; //得分
    private Long date; //时间戳

    public RecommendVideo() {
    }

    public RecommendVideo(ObjectId id, Long userId, Long videoId, Double score, Long date) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
        this.score = score;
        this.date = date;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RecommendVideo{" +
                "id=" + id +
                ", userId=" + userId +
                ", videoId=" + videoId +
                ", score=" + score +
                ", date=" + date +
                '}';
    }
}
