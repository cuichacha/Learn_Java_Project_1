package com.tanhua.commons.pojo.messages;

import java.io.Serializable;

public class MessageComment implements Serializable {
    private static final long serialVersionUID = 6003135946821874730L;

    private String id;
    private String avatar;
    private String nickname;
    private String createDate;

    public MessageComment() {
    }

    public MessageComment(String id, String avatar, String nickname, String createDate) {
        this.id = id;
        this.avatar = avatar;
        this.nickname = nickname;
        this.createDate = createDate;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MessageComment{" +
                "id='" + id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
