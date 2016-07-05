package com.baoxian.domain;

import javax.persistence.*;

@Entity
@Table(name = "wc_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String link;        //待抓链接
    private String uuid;        //唯一标识
    private Boolean achieve;    //完成状态

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getAchieve() {
        return achieve;
    }

    public void setAchieve(Boolean achieve) {
        this.achieve = achieve;
    }
}
