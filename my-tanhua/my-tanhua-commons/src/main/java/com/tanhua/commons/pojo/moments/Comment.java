package com.tanhua.commons.pojo.moments;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class Comment implements Serializable {
    private static final long serialVersionUID = -291788258125767614L;

    private ObjectId id;

    private ObjectId publishId; //发布id
    private Integer commentType; //评论类型，1-点赞，2-评论，3-喜欢
    private String content; //评论内容
    private Long userId; //评论人

    private Boolean isParent = false; //是否为父节点，默认是否
    private ObjectId parentId; // 父节点id

    private Long created; //发表时间

    public Comment() {
    }

    public Comment(ObjectId id, ObjectId publishId, Integer commentType, String content, Long userId, Boolean isParent, ObjectId parentId, Long created) {
        this.id = id;
        this.publishId = publishId;
        this.commentType = commentType;
        this.content = content;
        this.userId = userId;
        this.isParent = isParent;
        this.parentId = parentId;
        this.created = created;
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

    public ObjectId getPublishId() {
        return publishId;
    }

    public void setPublishId(ObjectId publishId) {
        this.publishId = publishId;
    }

    public Integer getCommentType() {
        return commentType;
    }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public ObjectId getParentId() {
        return parentId;
    }

    public void setParentId(ObjectId parentId) {
        this.parentId = parentId;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", publishId=" + publishId +
                ", commentType=" + commentType +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", isParent=" + isParent +
                ", parentId=" + parentId +
                ", created=" + created +
                '}';
    }
}
