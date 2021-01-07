package com.tanhua.commons.pojo.moments;

import java.io.Serializable;

public class MovementComment implements Serializable {

    private static final long serialVersionUID = -291788258125767619L;

    private String id; //评论id
    private String avatar; //头像
    private String nickname; //昵称
    private String content; //评论
    private String createDate; //评论时间: 08:27
    private Integer likeCount; //点赞数
    private Integer hasLiked; //是否点赞（1是，0否）

    public MovementComment() {
    }

    public MovementComment(String id, String avatar, String nickname, String content, String createDate, Integer likeCount, Integer hasLiked) {
        this.id = id;
        this.avatar = avatar;
        this.nickname = nickname;
        this.content = content;
        this.createDate = createDate;
        this.likeCount = likeCount;
        this.hasLiked = hasLiked;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(Integer hasLiked) {
        this.hasLiked = hasLiked;
    }

    @Override
    public String toString() {
        return "MovementComment{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                ", createDate='" + createDate + '\'' +
                ", likeCount=" + likeCount +
                ", hasLiked=" + hasLiked +
                '}';
    }
}
