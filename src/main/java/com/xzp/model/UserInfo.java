package com.xzp.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInfo {



    @Id//标识主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长策略

    private Long id;

    private Long userId;

    private String name;

    private String sex;

    private Date birthday;

    private String hobby;

    private String address;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby == null ? null : hobby.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }


    /**
     * value 中的 EL 表达式是判断 userInfo 的生日是否为空，如果不为空则显示其生日，
     * 主要是一个生日信息回显的作用。因为 UserInfo 实体类中没有 getFormateBirthday 方法
     * @return
     */
    public String getFormateBirthday(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(birthday);
    }
}