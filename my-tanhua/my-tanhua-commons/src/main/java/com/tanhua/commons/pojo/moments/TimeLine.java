package com.tanhua.commons.pojo.moments;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class TimeLine implements Serializable {
    private static final long serialVersionUID = 9096178416317502524L;
    private ObjectId id;

    private Long userId; // 好友id
    private ObjectId publishId; //发布id

    private Long date; //发布的时间

    public TimeLine() {
    }

    public TimeLine(ObjectId id, Long userId, ObjectId publishId, Long date) {
        this.id = id;
        this.userId = userId;
        this.publishId = publishId;
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

    public ObjectId getPublishId() {
        return publishId;
    }

    public void setPublishId(ObjectId publishId) {
        this.publishId = publishId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TimeLine{" +
                "id=" + id +
                ", userId=" + userId +
                ", publishId=" + publishId +
                ", date=" + date +
                '}';
    }
}
