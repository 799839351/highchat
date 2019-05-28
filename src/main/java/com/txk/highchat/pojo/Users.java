package com.txk.highchat.pojo;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Users {

    /**
     *主键id采用分布式雪花算法生成
     */
    /*@NotEmpty(message="主键id不能为空")
    @NotNull(message="主键id不能为null")*/
    private String id;

    /**
     * 用户名，账号，high聊号
     */
   /* @NotEmpty(message="high聊号不能为空")
    @NotNull(message="high聊号不能为null")*/
    private String username;
    /**
     * 密码
     */
  /*  @NotEmpty(message="密码不能为空")
    @NotNull(message="密码不能为null")*/
    private String password ;
    /**
     *  我的头像，如果没有默认给一张
     */
   /* @NotEmpty(message="头像不能为空")
    @NotNull(message="头像不能为null")*/
    private String faceimage  ;
    /**
     * 我的大头像图，如果没有默认给一张
     */
  /*  @NotEmpty(message="大头像图不能为空")
    @NotNull(message="大头像图不能为null")*/
    private String faceimagebig ;
    /**
     * 昵称
     */
  /*  @NotEmpty(message="昵称不能为空")
    @NotNull(message="昵称不能为null")*/
    private String  nickname ;
    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    /*@NotEmpty(message="二维码不能为空")
    @NotNull(message="二维码不能为null")*/
    private String qrcode ;
    /**
     * 客户端id
     */
    private String cid ;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFaceimage() {
        return faceimage;
    }

    public void setFaceimage(String faceimage) {
        this.faceimage = faceimage;
    }

    public String getFaceimagebig() {
        return faceimagebig;
    }

    public void setFaceimagebig(String faceimagebig) {
        this.faceimagebig = faceimagebig;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }



}