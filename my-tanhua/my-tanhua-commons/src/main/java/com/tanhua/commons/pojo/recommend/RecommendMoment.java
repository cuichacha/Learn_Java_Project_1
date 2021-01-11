package com.tanhua.commons.pojo.recommend;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "recommend_quanzi")
public class RecommendMoment implements Serializable {
    private static final long serialVersionUID = -4296017160071131963L;

    private ObjectId id;
    private Long userId;// 用户id
    private Long publishId; //动态id，需要转化为Long类型
    private Double score; //得分
    private Long date; //时间戳

    public RecommendMoment() {
    }

    public RecommendMoment(ObjectId id, Long userId, Long publishId, Double score, Long date) {
        this.id = id;
        this.userId = userId;
        this.publishId = publishId;
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

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
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
        return "RecommendMoment{" +
                "id=" + id +
                ", userId=" + userId +
                ", publishId=" + publishId +
                ", score=" + score +
                ", date=" + date +
                '}';
    }
}
