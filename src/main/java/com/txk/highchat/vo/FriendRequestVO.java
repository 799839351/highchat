package com.txk.highchat.vo;

/**
 * @Description: 好友请求发送方的信息
 */
public class FriendRequestVO {
	
    private String sendUserId;
    private String sendUserName;
    private String sendFaceImage;
    private String sendNickName;
    
	public String getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}
	public String getSendUsername() {
		return sendUserName;
	}
	public void setSendUsername(String sendUsername) {
		this.sendUserName = sendUsername;
	}
	public String getSendFaceImage() {
		return sendFaceImage;
	}
	public void setSendFaceImage(String sendFaceImage) {
		this.sendFaceImage = sendFaceImage;
	}
	public String getSendNickname() {
		return sendNickName;
	}
	public void setSendNickname(String sendNickname) {
		this.sendNickName = sendNickname;
	}
}