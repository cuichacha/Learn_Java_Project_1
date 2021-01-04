package com.tanhua.commons.vo.recommend;

import java.util.Arrays;

public class TodayBest {
    private Long id;
    private String avatar;
    private String nickname;
    private String gender; //性别 man woman
    private Integer age;
    private String[] tags;
    private Long fateValue; //缘分值

    public TodayBest() {
    }

    public TodayBest(Long id, String avatar, String nickname, String gender, Integer age, String[] tags, Long fateValue) {
        this.id = id;
        this.avatar = avatar;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.tags = tags;
        this.fateValue = fateValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getFateValue() {
        return fateValue;
    }

    public void setFateValue(Long fateValue) {
        this.fateValue = fateValue;
    }

    @Override
    public String toString() {
        return "TodayBest{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", tags=" + Arrays.toString(tags) +
                ", fateValue=" + fateValue +
                '}';
    }
}
