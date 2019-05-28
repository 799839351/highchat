package com.txk.highchat.pojo;


import java.util.Date;

public class FriendsRequest {
    /**
     * 主键id采用分布式雪花算法生成
     */
    private String id;
    /**
     * 发送消息用户的id
     */
    private String senduserid;
    /**
     * 接受消息用户的id
     */
    private String acceptuserid;
    /**
     * 发送请求的时间
     */
    private Date requestdatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenduserid() {
        return senduserid;
    }

    public void setSenduserid(String senduserid) {
        this.senduserid = senduserid;
    }

    public String getAcceptuserid() {
        return acceptuserid;
    }

    public void setAcceptuserid(String acceptuserid) {
        this.acceptuserid = acceptuserid;
    }

    public Date getRequestdatetime() {
        return requestdatetime;
    }

    public void setRequestdatetime(Date requestdatetime) {
        this.requestdatetime = requestdatetime;
    }
}
