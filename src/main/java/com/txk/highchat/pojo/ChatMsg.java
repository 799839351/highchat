package com.txk.highchat.pojo;


import java.util.Date;


public class ChatMsg {
    /**
     * 主键id采用分布式雪花算法生成
     */
    private String id;
    /**
     * 发送消息的用户id
     */
    private String senduserid;
    /**
     * 接受消息的用户id
     */
    private String acceptuserid;
    /**
     * 消息内容
     */
    private String msg;
    /**
     * 消息是否签收状态1：签收 0：未签收
     */
    private int signflag;
    /**
     * 创建时间，也就是发送消息的时间
     */
    private Date createtime;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSignflag() {
        return signflag;
    }

    public void setSignflag(int signflag) {
        this.signflag = signflag;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }


}