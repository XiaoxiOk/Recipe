package com.xi.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.MessageTypeEnum;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.common.grace.ResponseStatusEnum;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.model.pojo.Message;
import com.xi.model.pojo.User;
import com.xi.model.vo.FollowUserVO;
import com.xi.service.FollowService;
import com.xi.service.MessageService;
import com.xi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
@RequestMapping("/follow")
@Api(tags = "FollowController")
public class FollowController extends BaseInfoProperties {
    @Resource
    private FollowService followService;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @ApiOperation(value = "关注用户")
    @PostMapping("/doFollow")
    public GraceJSONResult doFollow(@RequestParam int fromUid, @RequestParam int toUid){
        // 判断两个id有效
        if (fromUid <= 0 || toUid <=  0) {
            return GraceJSONResult.notifyMsg("请确认用户ID有效");
        }

        // 判断当前用户，自己不能关注自己
        if (fromUid == toUid) {
            return GraceJSONResult.notifyMsg("用户不能关注自己！");
        }

        // 判断两个id对应的用户是否存在
        User TargetUser = userService.getUserById(toUid);
        User myInfo = userService.getUserById(fromUid);

        // 判断不可为空
        if (TargetUser == null || myInfo == null) {
            return GraceJSONResult.errorMsg("请确认用户信息有效！");
        }

        // 保存粉丝关系到数据库
       int count = followService.doFollow(fromUid, toUid);
        if(count == 1){
            log.info(fromUid+"关注:"+toUid+" 成功！");

            // 博主的粉丝+1，我的关注+1
            redis.increment(REDIS_MY_FOLLOWS_COUNTS + ":" + fromUid, 1);
            redis.increment(REDIS_MY_FANS_COUNTS + ":" + toUid, 1);
            // 我和其他发布者之间的关联关系
            redis.set(REDIS_USER_RELATIONSHIP + ":" + fromUid + ":" + toUid, "1");
            // 消息：通知关注
            Message message = new Message();
            message.setFromUid(fromUid);
            message.setToUid(toUid);
            message.setTypeId(toUid);
            message.setType(MessageTypeEnum.FOLLOW_YOU.type);
            messageService.insertMessage(message);
            return GraceJSONResult.ok("关注成功！");
        }else {
            return GraceJSONResult.errorMsg("关注失败！");
        }
    }


    @ApiOperation(value = "取关用户")
    @PostMapping("/doCancelFollow")
    public GraceJSONResult doCancelFollow(@RequestParam int fromUid, @RequestParam int toUid){
        // 删除业务的执行
        int count = followService.doCancelFollow(fromUid, toUid);
        if(count == 1){
            // 博主的粉丝-1，我的关注-1
            redis.decrement(REDIS_MY_FOLLOWS_COUNTS + ":" + fromUid, 1);
            redis.decrement(REDIS_MY_FANS_COUNTS + ":" + toUid, 1);

            // 我和博主的关联关系，依赖redis，不要存储数据库，避免db的性能瓶颈
            redis.del(REDIS_USER_RELATIONSHIP + ":" + fromUid + ":" + toUid);
            return GraceJSONResult.ok("取关成功！");
        }else {
            return GraceJSONResult.errorMsg("取关失败！");
        }
    }
    @ApiOperation(value = "判断是否关注对方")
    @PostMapping("/judgeDoIFollowUser")
    public GraceJSONResult queryDoIFollowUser(@RequestParam int fromUid, @RequestParam int toUid) {
        return GraceJSONResult.ok(followService.judgeDoIFollowUser(fromUid, toUid));
    }

    @ApiOperation(value = "分页获取我的关注用户列表")
    @PostMapping("/getMyFollowsByPage")
    public GraceJSONResult getMyFollowsByPage(@RequestBody Page page, @RequestParam int userId) {
        IPage<FollowUserVO> contentIPage = followService.getMyFollowsByPage(page,userId);
        return DuplicateMethod.getDataListByPage(contentIPage, page);
    }

    @ApiOperation(value = "分页获取我的粉丝用户列表")
    @PostMapping("/getMyFansByPage")
    public GraceJSONResult getMyFansByPage(@RequestBody Page page, @RequestParam int userId) {
        IPage<FollowUserVO> contentIPage = followService.getMyFansByPage(page,userId);
        System.out.println(contentIPage.getRecords().toString());
        return DuplicateMethod.getDataListByPage(contentIPage, page);
    }
    
}

