package com.tanhua.commons.pojo.moments;

import java.io.Serializable;
import java.util.Arrays;

public class Movements implements Serializable {
    private static final long serialVersionUID = 8732308321082804772L;

    private String id; //动态id
    private Long userId; //用户id
    private String avatar; //头像
    private String nickname; //昵称
    private String gender; //性别 man woman
    private Integer age; //年龄
    private String[] tags; //标签
    private String textContent; //文字动态
    private String[] imageContent; //图片动态
    private String distance; //距离
    private String createDate; //发布时间 如: 10分钟前
    private Integer likeCount; //点赞数
    private Integer commentCount; //评论数
    private Integer loveCount; //喜欢数
    private Integer hasLiked; //是否点赞（1是，0否）
    private Integer hasLoved; //是否喜欢（1是，0否）

    public Movements() {
    }

    public Movements(String id, Long userId, String avatar, String nickname, String gender, Integer age, String[] tags, String textContent, String[] imageContent, String distance, String createDate, Integer likeCount, Integer commentCount, Integer loveCount, Integer hasLiked, Integer hasLoved) {
        this.id = id;
        this.userId = userId;
        this.avatar = avatar;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.tags = tags;
        this.textContent = textContent;
        this.imageContent = imageContent;
        this.distance = distance;
        this.createDate = createDate;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.loveCount = loveCount;
        this.hasLiked = hasLiked;
        this.hasLoved = hasLoved;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(String[] imageContent) {
        this.imageContent = imageContent;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(Integer loveCount) {
        this.loveCount = loveCount;
    }

    public Integer getHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(Integer hasLiked) {
        this.hasLiked = hasLiked;
    }

    public Integer getHasLoved() {
        return hasLoved;
    }

    public void setHasLoved(Integer hasLoved) {
        this.hasLoved = hasLoved;
    }

    @Override
    public String toString() {
        return "Movements{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", tags=" + Arrays.toString(tags) +
                ", textContent='" + textContent + '\'' +
                ", imageContent=" + Arrays.toString(imageContent) +
                ", distance='" + distance + '\'' +
                ", createDate='" + createDate + '\'' +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", loveCount=" + loveCount +
                ", hasLiked=" + hasLiked +
                ", hasLoved=" + hasLoved +
                '}';
    }
}
