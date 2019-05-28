package com.txk.highchat.pojo;

public class MyFriends {

    /**
     * 主键id采用分布式雪花算法生成
     */
    private String   id ;
    /**
     * 我的用户id
     */
    private String  myuserid ;
    /**
     * 我朋友的用户id
     */
    private String  myfrienduserid ;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyuserid() {
        return myuserid;
    }

    public void setMyuserid(String myuserid) {
        this.myuserid = myuserid;
    }

    public String getMyfrienduserid() {
        return myfrienduserid;
    }

    public void setMyfrienduserid(String myfrienduserid) {
        this.myfrienduserid = myfrienduserid;
    }


}