package com.xi.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.util.SaResult;
import com.xi.common.grace.GraceJSONResult;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.common.utils.SaUserCheckLogin;
import com.xi.common.utils.StpUserUtil;
import com.xi.model.bo.RegistLoginBO;
import com.xi.model.bo.UserBO;
import com.xi.model.pojo.User;
import com.xi.model.vo.ShowUserVO;
import com.xi.model.vo.UserVO;
import com.xi.service.FollowService;
import com.xi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "UserController")
public class UserController extends BaseInfoProperties{

    @Resource
    private UserService userService;
    @Resource
    private FollowService followService;

    @ApiOperation(value = "判断用户是否注册，是则登录")
    @PostMapping("judgeIsRegisterAndLogin")
    public GraceJSONResult judgeIsRegisterAndLogin(@RequestParam("mobile") String mobile){
        User user = userService.queryMobileIsExist(mobile);
        if (user == null) {
            // 1 如果用户为空，表示没有注册过，则为null，需要提示用户进行注册信息录入
            Map<String, Boolean> result = new HashMap<>();
            result.put("needToRegister", true);
            return GraceJSONResult.ok(result);
        }

        // 2 如果用户为空，表示没有注册过，则为null，需要提示用户进行注册信息录入
        if(user.getFrozen()==1){
            return GraceJSONResult.notifyMsg("您的帐号已被冻结，暂不支持登录！");
        }
        // 3. 如果不为空，可以继续下方业务，可以保存用户会话信息
        // 3.1 登录会话时才会产生Session
        StpUserUtil.login(user.getId());
        log.info("SUCCESS：产生登录会话！");
        // 3.2，获取Token相关参数，并保存到redis中（自动！！）
        SaTokenInfo tokenInfo = StpUserUtil.getTokenInfo();
        StpUserUtil.getTokenSession().set("user",user);

        // 4，返回给前端 用户信息，包含token令牌
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(tokenInfo.tokenValue);
        userVO.setTokenInfo(tokenInfo);
        log.info("新注册的用户："+ userVO);
        return GraceJSONResult.ok(userVO);
    }

    @ApiOperation(value = "注册用户信息，并登录！")
    @PostMapping("registerAndLogin")
    public GraceJSONResult registerAndLogin(@Valid @RequestBody RegistLoginBO registLoginBO){

        User user = userService.createUser(registLoginBO);
        if(user == null){
            return GraceJSONResult.errorMsg("注册失败，待会儿再试试！");
        }else{
            log.info("SUCCESS：用户注册成功！");
        }
        // 3. 如果不为空，可以继续下方业务，可以保存用户会话信息
        // 3.1 登录会话时才会产生Session
        StpUserUtil.login(user.getId());
        log.info("SUCCESS：产生登录会话！");
        // 3.2，获取Token相关参数，并保存到redis中（自动！！）
        SaTokenInfo tokenInfo = StpUserUtil.getTokenInfo();
        StpUserUtil.getTokenSession().set("user",user);

        // 4，返回给前端 用户信息，包含token令牌
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(tokenInfo.tokenValue);
        userVO.setTokenInfo(tokenInfo);
        return GraceJSONResult.ok(userVO);
    }

    @ApiOperation(value = "注册/登录")
    @ApiImplicitParam(name = "registLoginBO", value = "前端传来的用户信息")
    @PostMapping("doLogin")
    public GraceJSONResult login(@Valid @RequestBody RegistLoginBO registLoginBO) {
        String mobile = registLoginBO.getMobile();
        // 1. 查询数据库，判断用户是否存在;也确保了mobile唯一
        User user = userService.queryMobileIsExist(mobile);
        if (user == null) {
            // 2.1 如果用户为空，表示没有注册过，则为null，需要注册信息入库
            user = userService.createUser(registLoginBO);
            if(user == null) {
                // 2.2 注册失败提示
                return GraceJSONResult.errorMsg("登录/注册失败，待会儿再试试！");
            }else{
                log.info("SUCCESS：用户注册成功！");
            }
        }else{
            //发现授权登录的用户名已经和之前存储的不同则更新数据库中的用户名
            if(!user.getUserName().equals(registLoginBO.getUserName())){
                int count = userService.updateUserNameById(user.getId(), registLoginBO.getUserName());
                if(count == 1){
                    log.info("SUCCESS：更换最新用户名成功");
                    user.setUserName(registLoginBO.getUserName());
                }
            }
            if(user.getFrozen()==1){
                return GraceJSONResult.notifyMsg("您的帐号处于禁用状态，请联系管理员");
            }
        }
        // 3. 如果不为空，可以继续下方业务，可以保存用户会话信息
        // 3.1 登录会话时才会产生Session
        StpUserUtil.login(user.getId());
        log.info("SUCCESS：产生登录会话！");
        // 3.2，获取Token相关参数，并保存到redis中（自动！！）
        SaTokenInfo tokenInfo = StpUserUtil.getTokenInfo();
        StpUserUtil.getTokenSession().set("user",user);

        // 4，返回给前端 用户信息，包含token令牌
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(tokenInfo.tokenValue);
        userVO.setTokenInfo(tokenInfo);

        return GraceJSONResult.ok(userVO);
    }

    @ApiOperation(value = "获取当前会话剩余有效期（单位：s，返回-1代表永久有效）")
    @SaUserCheckLogin
    @GetMapping("/getTokenTimeout")
    public SaResult getTokenTimeout(){
        return SaResult.ok(String.valueOf(StpUserUtil.getTokenTimeout()));
    }


    @ApiOperation(value = "根据Token值获取对应的账号id，如果未登录，则data为null")
    @ApiImplicitParam(name = "tokenValue ", value = "用户Token值")
    @GetMapping("/getUserByToken")
    public SaResult getUserByToken(@RequestBody String tokenValue){
        return SaResult.ok((String) StpUserUtil.getLoginIdByToken(tokenValue));
    }

    @ApiOperation(value = "获取一条用户信息--自己")
    @PostMapping("/getUserById")
    public GraceJSONResult getUserById(@RequestParam("userId") int userId){

        UserVO userVO = new UserVO();
        User user = userService.getUserById(userId);
        // 我的关注博主总数量
        int myFollowsCounts = 0;
        // 我的粉丝总数
        int myFansCounts = 0;

        String follow_str = REDIS_MY_FOLLOWS_COUNTS + ":" + userId;
        String fan_str = REDIS_MY_FANS_COUNTS + ":" + userId;


        if(redis.keyIsExist(follow_str)){
            String myFollowsCountsStr = redis.get(follow_str);
            if (StringUtils.isNotBlank(myFollowsCountsStr)) {
                myFollowsCounts= Integer.parseInt(myFollowsCountsStr);
            }
        }
        if(myFollowsCounts <= 0){
            myFollowsCounts = followService.getMyFollowsCounts(userId);
        }
        if(redis.keyIsExist(fan_str)){
            String myFansCountsStr = redis.get(fan_str);
            if (StringUtils.isNotBlank(myFansCountsStr)) {
                myFansCounts  = Integer.parseInt(myFansCountsStr);
            }
        }
        if(myFansCounts <= 0){
            myFansCounts = followService.getMyFansCounts(userId);
        }
        BeanUtils.copyProperties(user, userVO);
        userVO.setMyFollowsCounts(myFollowsCounts);
        userVO.setMyFansCounts(myFansCounts);

        return GraceJSONResult.ok(userVO);
    }

    @ApiOperation(value = "获取一条用户信息--点击查看")
    @GetMapping("/getShowUserById")
    public GraceJSONResult getShowUserById(@RequestParam("userId") int userId, @RequestParam("myId") int myId){

        User userBasic = userService.getUserById(userId);
        ShowUserVO showUserVO = new ShowUserVO();

        BeanUtils.copyProperties(userBasic, showUserVO);
        // 我的关注博主总数量
        int thisFollowsCounts = 0;
        String follow_str = REDIS_MY_FOLLOWS_COUNTS + ":" + userId;
        if(redis.keyIsExist(follow_str)){
            String thisFollowsCountsStr = redis.get(follow_str);
            if (StringUtils.isNotBlank(thisFollowsCountsStr)) {
                thisFollowsCounts= Integer.parseInt(thisFollowsCountsStr);
            }
        }

        // 我的粉丝总数
        int thisFansCounts = 0;
        String fan_str = REDIS_MY_FANS_COUNTS + ":" + userId;
        if(redis.keyIsExist(fan_str)){
            String thisFansCountsStr = redis.get(fan_str);
            if (StringUtils.isNotBlank(thisFansCountsStr)) {
                thisFansCounts  = Integer.parseInt(thisFansCountsStr);
            }
        }


        showUserVO.setThisFollowsCounts(thisFollowsCounts);
        showUserVO.setThisFansCounts(thisFansCounts);

        /* 判断"我"是否关注发布该菜谱的作者 */
        showUserVO.setDoIFollowThisUser(followService.judgeDoIFollowUser(myId, userId));

        return GraceJSONResult.ok(showUserVO);
    }

    @ApiOperation(value = "用户更新自己信息")
    @PostMapping("/updateUserById")
    public GraceJSONResult updateUserById(@RequestBody UserBO userBO){
        User userToUpdate = new User();
        BeanUtils.copyProperties(userBO, userToUpdate);
        if(userService.updateUserById(userToUpdate) == 1){
            return GraceJSONResult.ok("更新成功！");
        }else{
            return GraceJSONResult.errorMsg("操作失败！");
        }
    }
    @ApiOperation(value = "用户修改性别信息")
    @SaUserCheckLogin
    @PostMapping("/updateGenderById")
    public GraceJSONResult updateGenderById(@RequestParam("userId") int userId, @RequestParam("gender") int gender){
        if(userService.updateGenderById(userId, gender) == 1){
            return GraceJSONResult.ok("操作成功！");
        }else{
            return GraceJSONResult.errorMsg("操作失败！");
        }
    }

    @ApiOperation(value = "用户修改生日信息")
    @SaUserCheckLogin
    @PostMapping("/updateBirthdayById")
    public GraceJSONResult updateBirthdayById(@RequestParam("userId") int userId, @RequestParam("birthday") Date birthday){
        if(userService.updateBirthdayById(userId, birthday) == 1){
            return GraceJSONResult.ok("操作成功！");
        }else{
            return GraceJSONResult.errorMsg("操作失败！");
        }
    }

    //TODO 完成用户更换头像！


    @ApiOperation(value = "用户请求退出登录，注销会话")
    @PostMapping("logout")
    public GraceJSONResult logout(@RequestParam("userId") int userId) {
        System.out.println("user[id="+userId+"] want to logout!");
        StpUserUtil.logout(userId);//退出登录，且自动删除redis缓存信息
        return GraceJSONResult.ok("会话ID为 " + userId + " 的用户退出登录成功");
    }


    @ApiOperation(value = "查询登录状态")
    @GetMapping("/isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUserUtil.isLogin();
    }

}

