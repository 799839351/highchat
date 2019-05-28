package com.txk.highchat.mapper;

import com.txk.highchat.pojo.ChatMsg;
import com.txk.highchat.pojo.FriendsRequest;
import com.txk.highchat.pojo.MyFriends;
import com.txk.highchat.pojo.Users;
import com.txk.highchat.vo.FriendRequestVO;
import com.txk.highchat.vo.MyFriendsVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserDao {
    /**
     * @Description: 判断用户名是否存在
     */
    @Select("SELECT * FROM users WHERE username=#{username} LIMIT 1")
    Users queryUsernameIsExist(String username);

    /**
     * @Description: 查询用户是否存在
     */
    @Select("SELECT * FROM users WHERE username=#{username} AND `password`=#{password}  LIMIT 1")
    Users queryUserForLogin(Users user);

    /**
     * @Description: 用户注册
     */
    @Insert("INSERT INTO users (id, username, password, faceimage, faceimagebig, nickname , qrcode ,cid) " +
            "VALUES (#{id}, #{username}, #{password}, #{faceimage}, #{faceimagebig}, #{nickname}, #{qrcode}, #{cid})")
    int saveUser(Users user);

    /**
     * @Description: 修改用户记录
     */
    @Update("UPDATE users SET faceimage=#{faceimage} , faceimagebig=#{faceimagebig} " +
            " WHERE id=#{id}")
    int updateUserInfo(Users user);

    /**
     * @Description: 根据主键查找
     */
    @Select("SELECT * FROM users WHERE id=#{id}")
    Users queryUserById(String id);

    /**
     * @Description: 修改用户昵称
     */
    @Update("UPDATE users SET nickname=#{nickname} WHERE id=#{id}")
    int setNickname(Users user);

    /**
     * @Description: 根据用户名查询用户对象
     */
    @Select("SELECT * FROM users WHERE username=#{username}  LIMIT 1")
    Users queryUserInfoByUsername(String username);

    /**
     * 根据用户主键和我的账号的主键判断是不是好友
     * @param myFriends
     * @return
     */
   @Select("SELECT  * from myfriend WHERE myfrienduserid=#{myfrienduserid} AND myuserid=#{myuserid} LIMIT 1 ")
    MyFriends existFriend(MyFriends myFriends);


    /**
     * @Description: 添加好友请求记录，保存到数据库
     */
   @Insert("INSERT INTO friendrequest(id,senduserid,acceptuserid,requestdatetime) " +
           "VALUES(#{id},#{senduserid},#{acceptuserid},#{requestdatetime})")
    int saveFriendRequest(FriendsRequest friendsRequest);

   /**
    * @Description: 根据我的id和朋友的id查询我的好友表里是否存在记录
     */
   @Select("SELECT *  from friendrequest   where senduserid=#{senduserid} and acceptuserid=#{acceptuserid} LIMIT 1")
    FriendsRequest queryFriendRequestByCondition(FriendsRequest friendsRequest);

    /**
     * @Description: 查询好友请求
     */
    @Select("SELECT \n" +
            "sender.id AS sendUserId ,\n" +
            "sender.username AS sendUserName ,\n" +
            "sender.faceimage AS sendFaceImage,\n" +
            "sender.nickname AS sendNickName \n" +
            "from friendrequest fr \n" +
            "LEFT JOIN users sender \n" +
            "ON fr.senduserid=sender.id WHERE fr.acceptuserid=#{acceptuserid}")

    List<FriendRequestVO> queryFriendRequestList(String acceptuserid);

    /**
     * @Description: 查询好友列表
     */
    @Select("SELECT u.id AS friendUserId,\n" +
            "       u.username AS friendUsername,\n" +
            "       u.faceimage AS friendFaceImage,\n" +
            "       u.nickname AS friendNickname\n" +
            "\n" +
            " from myfriend mf LEFT JOIN users u\n" +
            " ON u.id=mf.myfrienduserid WHERE mf.myuserid=#{myuserid}")
    List<MyFriendsVO> queryMyFriends(String myuserid);



    /**
     * @Description: 批量签收消息
     */
    @Update("<script>"+
                      "UPDATE chatmsg SET signflag= 1 WHERE id in "+
                     "<foreach item='item' index='index' " +
                              "collection='strList'" +
                             " open='(' separator=',' close=')'>"+
                              "#{item}"
                          + "</foreach>"
            +"</script>")
    int updateMsgSigned(@Param("strList") List<String> strList);

    /**
     * @Description: 获取未签收消息列表
     */
    @Select("SELECT * FROM chatmsg WHERE signflag=0 AND acceptuserid=#{acceptuserid}")
    List<ChatMsg> getUnReadMsgList(String acceptuserid);

    @Delete("DELETE  from friendrequest WHERE senduserid=#{senduserid} AND acceptuserid=#{acceptuserid}")
    int deleteFriendRequest(FriendsRequest friendsRequest);

    @Insert("INSERT INTO myfriend(myfrienduserid,myuserid,id)VALUES(#{myfrienduserid},#{myuserid},#{id})")
    int insert(MyFriends myFriends);

    @Insert("INSERT INTO chatmsg(id,senduserid,acceptuserid,msg,signflag,createtime)\n" +
            "VALUES(#{id},#{senduserid},#{acceptuserid},#{msg},#{signflag},#{createtime})")
    int insertChatMsg(ChatMsg chatMsg);
}
