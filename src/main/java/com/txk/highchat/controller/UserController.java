package com.txk.highchat.controller;

import com.txk.highchat.bo.UsersBO;
import com.txk.highchat.enums.OperatorFriendRequestTypeEnum;
import com.txk.highchat.enums.SearchFriendsStatusEnum;
import com.txk.highchat.pojo.ChatMsg;
import com.txk.highchat.utils.*;
import com.txk.highchat.vo.MyFriendsVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import com.txk.highchat.pojo.Users;
import com.txk.highchat.service.UserService;
import com.txk.highchat.vo.UsersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
@Api("用户控制器")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private Sid sid;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @ApiOperation(value = "用户注册")
    @PostMapping("/registOrLogin")
    public JsonResult<String> registOrLogin(@Valid @RequestBody Users users) throws Exception {
        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(users.getUsername())
                || StringUtils.isBlank(users.getPassword())) {
            return JsonResult.errorMsg("用户名或密码不能为空...");
        }
        // 1. 判断用户名是否存在，如果存在就登录，如果不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(users.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            // 1.1 登录
            userResult = userService.queryUserForLogin(users.getUsername(),
                    MD5Utils.getMD5Str(users.getPassword()));
            if (userResult == null) {
                return JsonResult.errorMsg("用户名或密码不正确...");
            }
        } else {
            // 1.2 注册
            users.setNickname(users.getUsername());
            users.setFaceimage("");
            users.setFaceimagebig("");
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
            users.setId(sid.nextShort());
            // TODO 为每个用户生成一个唯一的二维码稍后补充
            String  qrCodePath="H:\\" + users.getId() + "qrCode.png";
            qrCodeUtils.createQRCode(qrCodePath,"highchat_qrcode:"+users.getUsername());
            // 上传文件到fastdfs
            MultipartFile  qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
            String url = fastDFSClient.uploadBase64(qrCodeFile);
            users.setQrcode(url);

            userResult = userService.saveUser(users);
        }
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userResult, userVO);


        return JsonResult.success(userVO);
    }


    @PostMapping("/uploadFaceBase64")
    @ApiOperation(value = "上传用户头像")
    public JsonResult uploadFaceBase64(@RequestBody UsersBO userBO) throws Exception {

        // 获取前端传过来的base64字符串, 然后转换为文件对象再上传
        String base64Data = userBO.getFaceData();
        String userFacePath = "H:\\" + userBO.getUserId() + ".png";
        FileUtils.base64ToFile(userFacePath, base64Data);

        // 上传文件到fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        System.out.println(url);

        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];
        // 更新用户头像
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setFaceimage(thumpImgUrl);
        user.setFaceimagebig(url);


        Users result = userService.updateUserInfo(user);

        return JsonResult.success(result);
    }
    @PostMapping("/setNickname")
    @ApiOperation(value = "用户修改昵称")
    public JsonResult setNickname(@RequestBody UsersBO userBO) throws Exception {


        // 更新用户头像
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());


        Users result = userService.setNickname(user);

        return JsonResult.success(result);
    }

    @PostMapping("/searchUser")
    @ApiOperation(value = "查找用户")
    public JsonResult searchUser(String myUserId, String friendUsername) throws Exception {

        // 0. 判断 myUserId friendUsername 不能为空
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return JsonResult.errorMsg("用户名不能为空");
        }

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            Users user = userService.queryUserInfoByUsername(friendUsername);
            UsersVO userVO = new UsersVO();
            BeanUtils.copyProperties(user, userVO);
            return JsonResult.success(userVO);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return JsonResult.errorMsg(errorMsg);
        }
    }

    @PostMapping("/addFriendRequest")
    @ApiOperation(value = "添加好友")
    public JsonResult addFriendRequest(String myUserId, String friendUsername) throws Exception {

        // 0. 判断 myUserId friendUsername 不能为空
        if (StringUtils.isBlank(myUserId)
                || StringUtils.isBlank(friendUsername)) {
            return JsonResult.errorMsg("");
        }

        // 前置条件 - 1. 搜索的用户如果不存在，返回[无此用户]
        // 前置条件 - 2. 搜索账号是你自己，返回[不能添加自己]
        // 前置条件 - 3. 搜索的朋友已经是你的好友，返回[该用户已经是你的好友]
        Integer status = userService.preconditionSearchFriends(myUserId, friendUsername);
        if (status == SearchFriendsStatusEnum.SUCCESS.status) {
            userService.sendFriendRequest(myUserId, friendUsername);
        } else {
            String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
            return JsonResult.errorMsg(errorMsg);
        }

        return JsonResult.success(null);

    }
    /**
     * @Description: 查询发送添加好友的请求
     */
    @ApiOperation(value = "发送添加好友的请求")
    @PostMapping("/queryFriendRequests")
    public JsonResult queryFriendRequests(String userId) {

        // 0. 判断不能为空
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }
        // 1. 查询用户接受到的朋友申请
        return JsonResult.success(userService.queryFriendRequestList(userId));
    }



    /**
     * @Description: 接受方通过或者忽略朋友请求
     */
    @PostMapping("/operFriendRequest")
    @ApiOperation(value = "接受方通过或者忽略朋友请求")

    public JsonResult operFriendRequest(String acceptUserId, String sendUserId,
                                             Integer operType) {

        // 0. acceptUserId sendUserId operType 判断不能为空
        if (StringUtils.isBlank(acceptUserId)
                || StringUtils.isBlank(sendUserId)
                || operType == null) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        // 1. 如果operType 没有对应的枚举值，则直接抛出空错误信息
        if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
            return JsonResult.errorMsg("操作类型不能为空");
        }

        if (operType == OperatorFriendRequestTypeEnum.IGNORE.type) {
            // 2. 判断如果忽略好友请求，则直接删除好友请求的数据库表记录
            userService.deleteFriendRequest(sendUserId, acceptUserId);
        } else if (operType == OperatorFriendRequestTypeEnum.PASS.type) {
            // 3. 判断如果是通过好友请求，则互相增加好友记录到数据库对应的表
            //    然后删除好友请求的数据库表记录
            userService.passFriendRequest(sendUserId, acceptUserId);
        }

        // 4. 数据库查询好友列表
        List<MyFriendsVO> myFirends = userService.queryMyFriends(acceptUserId);

        return JsonResult.success(null);
    }

    /**
     * @Description: 查询我的好友列表
     */
    @PostMapping("/myFriends")
    @ApiOperation(value = "查询我的好友列表")
    public JsonResult myFriends(String userId) {
        // 0. userId 判断不能为空
        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("");
        }

        // 1. 数据库查询好友列表
        List<MyFriendsVO> myFirends = userService.queryMyFriends(userId);

        return JsonResult.success(myFirends);
    }

    /**
     *
     * @Description: 用户手机端获取未签收的消息列表
     */
    @PostMapping("/getUnReadMsgList")
    @ApiOperation(value = "用户手机端获取未签收的消息列表")
    public JsonResult getUnReadMsgList(String acceptUserId) {
        // 0. userId 判断不能为空
        if (StringUtils.isBlank(acceptUserId)) {
            return JsonResult.errorMsg("用户id不能为空");
        }

        // 查询列表
        List<ChatMsg> unreadMsgList = userService.getUnReadMsgList(acceptUserId);

        return JsonResult.success(unreadMsgList);
    }

}
