package com.tanhua.commons.pojo.videos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "video")
public class Video implements Serializable {
    private static final long serialVersionUID = -3136732836884933873L;

    private ObjectId id; //主键id
    private Long userId;
    private String text; //文字
    private String picUrl; //视频封面文件
    private String videoUrl; //视频文件
    private Long created; //创建时间
    private Integer seeType; // 谁可以看，1-公开，2-私密，3-部分可见，4-不给谁看
    private List<Long> seeList; //部分可见的列表
    private List<Long> notSeeList; //不给谁看的列表
    private String longitude; //经度
    private String latitude; //纬度
    private String locationName; //位置名称

    public Video() {
    }

    public Video(ObjectId id, Long userId, String text, String picUrl, String videoUrl, Long created, Integer seeType, List<Long> seeList, List<Long> notSeeList, String longitude, String latitude, String locationName) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.picUrl = picUrl;
        this.videoUrl = videoUrl;
        this.created = created;
        this.seeType = seeType;
        this.seeList = seeList;
        this.notSeeList = notSeeList;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationName = locationName;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
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

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", created=" + created +
                ", seeType=" + seeType +
                ", seeList=" + seeList +
                ", notSeeList=" + notSeeList +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
