package com.tanhua.sso.pojo;

import com.tanhua.sso.enums.SexEnum;

public class UserInfo extends BasePojo {
    private Long id;
    private Long userId; //用户id
    private String nickName; //昵称
    private String logo; //用户头像
    private String tags; //用户标签：多个用逗号分隔
    private SexEnum sex; //性别
    private Integer age; //年龄
    private String edu; //学历
    private String city; //城市
    private String birthday; //生日
    private String coverPic; // 封面图片
    private String industry; //行业
    private String income; //收入
    private String marriage; //婚姻状态

    public UserInfo() {
    }

    public UserInfo(Long id, Long userId, String nickName, String logo, String tags, SexEnum sex, Integer age, String edu, String city, String birthday, String coverPic, String industry, String income, String marriage) {
        this.id = id;
        this.userId = userId;
        this.nickName = nickName;
        this.logo = logo;
        this.tags = tags;
        this.sex = sex;
        this.age = age;
        this.edu = edu;
        this.city = city;
        this.birthday = birthday;
        this.coverPic = coverPic;
        this.industry = industry;
        this.income = income;
        this.marriage = marriage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", logo='" + logo + '\'' +
                ", tags='" + tags + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", edu='" + edu + '\'' +
                ", city='" + city + '\'' +
                ", birthday='" + birthday + '\'' +
                ", coverPic='" + coverPic + '\'' +
                ", industry='" + industry + '\'' +
                ", income='" + income + '\'' +
                ", marriage='" + marriage + '\'' +
                '}';
    }
}
