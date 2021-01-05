package com.tanhua.commons.pojo.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class User extends BasePojo implements Serializable {
    private static final long serialVersionUID = -4296017160071130965L;

    private Long id;
    private String mobile;
    @JsonIgnore
    private String password;

    public User() {
    }

    public User(Long id, String mobile, String password) {
        this.id = id;
        this.mobile = mobile;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
