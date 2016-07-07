package com.baoxian.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "wc_agent")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;            //姓名
    private String sex;             //性别
    private String phone;           //电话
    private String wx;              //微信号
    private String qq;              //QQ号
    private String company;         //所属公司
    private String location;        //执业区域
    private String business;        //业务范围
    @Column(length = 16777216)
    private String introduce;       //个人介绍
    @NotNull
    private String uuid;            //唯一标识
    @NotNull
    private String stamp;           //时间戳

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        if (stamp == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.stamp = dateFormat.format(new Date());
        } else {
            this.stamp = stamp;
        }
    }

    public Agent() {
        super();
    }
}
