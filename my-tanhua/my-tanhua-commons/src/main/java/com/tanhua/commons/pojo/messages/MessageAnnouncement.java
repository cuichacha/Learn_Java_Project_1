package com.tanhua.commons.pojo.messages;

import java.io.Serializable;

public class MessageAnnouncement implements Serializable {
    private static final long serialVersionUID = 6003135946821871230L;

    private String id;
    private String title;
    private String description;
    private String createDate;

    public MessageAnnouncement() {
    }

    public MessageAnnouncement(String id, String title, String description, String createDate) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MessageAnnouncement{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
