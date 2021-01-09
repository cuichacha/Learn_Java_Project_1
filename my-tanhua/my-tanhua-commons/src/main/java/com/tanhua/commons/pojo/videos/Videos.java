package com.tanhua.commons.pojo.videos;

import java.io.Serializable;

public class Videos implements Serializable {
    private static final long serialVersionUID = -3136732836884933876L;

    private String id;
    private Long userId;
    private String avatar; //头像
    private String nickname; //昵称
    private String cover; //封面
    private String videoUrl; //视频URL
    private String signature; //签名
    private Integer likeCount; //点赞数量
    private Integer hasLiked; //是否已赞（1是，0否）
    private Integer hasFocus; //是是否关注 （1是，0否）
    private Integer commentCount; //评论数量

    public Videos() {
    }

    public Videos(String id, Long userId, String avatar, String nickname, String cover, String videoUrl, String signature, Integer likeCount, Integer hasLiked, Integer hasFocus, Integer commentCount) {
        this.id = id;
        this.userId = userId;
        this.avatar = avatar;
        this.nickname = nickname;
        this.cover = cover;
        this.videoUrl = videoUrl;
        this.signature = signature;
        this.likeCount = likeCount;
        this.hasLiked = hasLiked;
        this.hasFocus = hasFocus;
        this.commentCount = commentCount;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public Integer getHasFocus() {
        return hasFocus;
    }

    public void setHasFocus(Integer hasFocus) {
        this.hasFocus = hasFocus;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "Videos{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", cover='" + cover + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", signature='" + signature + '\'' +
                ", likeCount=" + likeCount +
                ", hasLiked=" + hasLiked +
                ", hasFocus=" + hasFocus +
                ", commentCount=" + commentCount +
                '}';
    }
}
