package com.tanhua.commons.pojo.moments;

import org.bson.types.ObjectId;

import java.io.Serializable;

public class Album implements Serializable {
    private static final long serialVersionUID = 432183095092216817L;

    private ObjectId id; //主键id

    private ObjectId publishId; //发布id
    private Long created; //发布时间

    public Album() {
    }

    public Album(ObjectId id, ObjectId publishId, Long created) {
        this.id = id;
        this.publishId = publishId;
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

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", publishId=" + publishId +
                ", created=" + created +
                '}';
    }
}
