package com.tanhua.commons.pojo.recommend;

import java.io.Serializable;

public class QueryUser implements Serializable {
    private static final long serialVersionUID = -4296017160071130963L;

    private Integer page = 1; //当前页数
    private Integer pagesize = 10; //页尺寸
    private String gender; //性别 man woman
    private String lastLogin; //近期登陆时间
    private Integer age; //年龄
    private String city; //居住地
    private String education; //学历

    public QueryUser() {
    }

    public QueryUser(Integer page, Integer pagesize, String gender, String lastLogin, Integer age, String city, String education) {
        this.page = page;
        this.pagesize = pagesize;
        this.gender = gender;
        this.lastLogin = lastLogin;
        this.age = age;
        this.city = city;
        this.education = education;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Override
    public String toString() {
        return "QueryUser{" +
                "page=" + page +
                ", pagesize=" + pagesize +
                ", gender='" + gender + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", education='" + education + '\'' +
                '}';
    }
}
