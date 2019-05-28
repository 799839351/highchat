package com.txk.highchat.service;

import com.txk.highchat.enums.MsgActionEnum;
import com.txk.highchat.enums.MsgSignFlagEnum;
import com.txk.highchat.enums.SearchFriendsStatusEnum;
import com.txk.highchat.mapper.UserDao;
import com.txk.highchat.netty.ChatMsg;
import com.txk.highchat.netty.DataContent;
import com.txk.highchat.netty.UserChannelRel;
import com.txk.highchat.pojo.FriendsRequest;
import com.txk.highchat.pojo.MyFriends;
import com.txk.highchat.pojo.Users;
import com.txk.highchat.utils.JsonUtils;
import com.txk.highchat.vo.FriendRequestVO;
import com.txk.highchat.vo.MyFriendsVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private Sid sid;

    /**
     * @Description: 用户注册
     */
    public Users saveUser(Users user) {
        int i = userDao.saveUser(user);
        if (i > 0) {
            return user;
        }
        return null;
    }

    /**
     * @Description: 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username) {
        Users users = userDao.queryUsernameIsExist(username);
        if (users != null) {
            return true;
        }
        return false;
    }

    /**
     * @Description: 查询用户是否存在
     */

    public Users queryUserForLogin(String username, String password) {
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(password);
        return userDao.queryUserForLogin(users);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public Users updateUserInfo(Users user) {
        int i = userDao.updateUserInfo(user);
        return userDao.queryUserById(user.getId());
    }


    /**
     * 设置用户昵称
     *
     * @param user
     * @return
     */
    public Users setNickname(Users user) {
        int i = userDao.setNickname(user);
        return userDao.queryUserById(user.getId());
    }

    /**
     * @Description: 搜索朋友的前置条件
     */
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        Users users = queryUserInfoByUsername(friendUsername);
        if (users == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        if (myUserId.equals(users.getId())) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        MyFriends myFriends = new MyFriends();
        myFriends.setMyfrienduserid(users.getId());
        myFriends.setMyuserid(myUserId);

        MyFriends myFriendsRef = userDao.existFriend(myFriends);
        if (myFriendsRef != null)
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        return SearchFriendsStatusEnum.SUCCESS.status;
    }


    /**
     * 根据用户名查询用户的信息
     *
     * @param username
     * @return
     */
    public Users queryUserInfoByUsername(String username) {
        return userDao.queryUserInfoByUsername(username);
    }

    /**
     * 发送好友请求
     *
     * @param myUserId
     * @param friendUsername
     */
    public void sendFriendRequest(String myUserId, String friendUsername) {
        //根据用户名把朋友的信息查询出来
        Users friend = queryUserInfoByUsername(friendUsername);
        //查询发送好友请求的记录表
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setSenduserid(myUserId);
        friendsRequest.setAcceptuserid(friend.getId());
        FriendsRequest friendsRequestRes = userDao.queryFriendRequestByCondition(friendsRequest);

        if (friendsRequestRes == null) {
            friendsRequest.setRequestdatetime(new Date());
            friendsRequest.setId(sid.nextShort());
            userDao.saveFriendRequest(friendsRequest);
        }

    }

    /**
     * 查询好友请求
     * @param acceptuserid
     * @return
     */
    public List<FriendRequestVO> queryFriendRequestList(String acceptuserid) {
        return userDao.queryFriendRequestList(acceptuserid);
    }

    /**
     * 删除好友请求（忽略好友请求）
     * @param sendUserId
     * @param acceptUserId
     */
    public void deleteFriendRequest(String sendUserId, String acceptUserId) {
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setAcceptuserid(acceptUserId);
        friendsRequest.setSenduserid(sendUserId);
       int res= userDao.deleteFriendRequest(friendsRequest);
    }
    /**
     * 通过好友请求
     * ①保存好友，
     * ②逆向保存好友
     * 三删除好友的的请求记录
     * @param sendUserId
     * @param acceptUserId
     */
   @Transactional
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        saveFriends(sendUserId,acceptUserId);
        saveFriends(acceptUserId,sendUserId);
        deleteFriendRequest(sendUserId,acceptUserId);
       Channel sendChannel = UserChannelRel.get(sendUserId);
       if (sendChannel != null) {
           // 使用websocket主动推送消息到请求发起者，更新他的通讯录列表为最新
           DataContent dataContent = new DataContent();
           dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);
           sendChannel.writeAndFlush(
                   new TextWebSocketFrame(
                           JsonUtils.objectToJson(dataContent)));
       }
    }

    /**
     * 获取好友列表
     * @param userId
     * @return
     */
    public List<MyFriendsVO> queryMyFriends(String userId) {
        return  userDao.queryMyFriends(userId);
    }

    /**
     * 保存好友
     * @param sendUserId
     * @param acceptUserId
     */
    private void saveFriends(String sendUserId, String acceptUserId) {
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();
        myFriends.setId(recordId);
        myFriends.setMyfrienduserid(acceptUserId);
        myFriends.setMyuserid(sendUserId);
        userDao.insert(myFriends);
    }


    /**
     * 保存消息
     * @param msg
     * @return
     */
    public String saveMsg(ChatMsg msg){
       com.txk.highchat.pojo.ChatMsg chatMsg=new    com.txk.highchat.pojo.ChatMsg();
        chatMsg.setId(sid.nextShort());
        chatMsg.setSenduserid(msg.getSenderId());
        chatMsg.setAcceptuserid(msg.getReceiverId());
        chatMsg.setCreatetime(new Date());
        chatMsg.setSignflag(MsgSignFlagEnum.unsign.type);
        chatMsg.setMsg(msg.getMsg());
        userDao.insertChatMsg(chatMsg);
        return chatMsg.getId();
    }

    /**
     * 批量签收消息
     * @param msgIdList
     */
  @Transactional
    public void updateMsgSigned(List<String> msgIdList) {
      userDao.updateMsgSigned(msgIdList);
    }

    public List<com.txk.highchat.pojo.ChatMsg> getUnReadMsgList(String acceptUserId) {
      return   userDao.getUnReadMsgList(acceptUserId);
    }
}
