package com.tanhua.commons.pojo.moments;

import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

public class Publish implements Serializable {
    private static final long serialVersionUID = 8732308321082804771L;

    private ObjectId id; //主键id
    private Long userId;
    private String text; //文字
    private List<String> medias; //媒体数据，图片或小视频 url
    private Integer seeType; // 谁可以看，1-公开，2-私密，3-部分可见，4-不给谁看
    private List<Long> seeList; //部分可见的列表
    private List<Long> notSeeList; //不给谁看的列表
    private String longitude; //经度
    private String latitude; //纬度
    private String locationName; //位置名称
    private Long created; //发布时间

    public Publish() {
    }

    public Publish(ObjectId id, Long userId, String text, List<String> medias, Integer seeType, List<Long> seeList, List<Long> notSeeList, String longitude, String latitude, String locationName, Long created) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.medias = medias;
        this.seeType = seeType;
        this.seeList = seeList;
        this.notSeeList = notSeeList;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getMedias() {
        return medias;
    }

    public void setMedias(List<String> medias) {
        this.medias = medias;
    }

    public Integer getSeeType() {
        return seeType;
    }

    public void setSeeType(Integer seeType) {
        this.seeType = seeType;
    }

    public List<Long> getSeeList() {
        return seeList;
    }

    public void setSeeList(List<Long> seeList) {
        this.seeList = seeList;
    }

    public List<Long> getNotSeeList() {
        return notSeeList;
    }

    public void setNotSeeList(List<Long> notSeeList) {
        this.notSeeList = notSeeList;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Publish{" +
                "id=" + id +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                ", medias=" + medias +
                ", seeType=" + seeType +
                ", seeList=" + seeList +
                ", notSeeList=" + notSeeList +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", locationName='" + locationName + '\'' +
                ", created=" + created +
                '}';
    }
}
