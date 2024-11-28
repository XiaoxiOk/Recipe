package com.xi.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(tags = "MessageController")
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @ApiOperation("获取关注消息")
    @PostMapping("/getFollowMsgList")
    public GraceJSONResult getFollowMsgList(@RequestBody Page page, @RequestParam("userId") int userId){
       IPage<Map<String, Object>> contentIPage = messageService.getFollowMessage(page, userId);
       return DuplicateMethod.getDataListByPage(contentIPage, page);
    }

    @ApiOperation("获取点赞消息")
    @PostMapping("/getLikeMsgList")
    public GraceJSONResult getLikeMsgList(@RequestBody Page page, @RequestParam("userId") int userId){
        IPage<Map<String, Object>> contentIPage = messageService.getLikeMessage(page, userId);
        return DuplicateMethod.getDataListByPage(contentIPage, page);
    }

    @ApiOperation("获取评论/回复消息")
    @PostMapping("/getCommentMsgList")
    public GraceJSONResult getCommentMsgList(@RequestBody Page page, @RequestParam("userId") int userId){
        IPage<Map<String, Object>> contentIPage = messageService.getCommentMessage(page, userId);
        return DuplicateMethod.getDataListByPage(contentIPage, page);
    }
    @ApiOperation("删除消息")
    @PostMapping("/deleteMsgById")
    public GraceJSONResult deleteMsgById( @RequestParam("msgId") int msgId){
        int count = messageService.deleteMsgById(msgId);
        if(count == 1){
            return GraceJSONResult.ok();
        }else{
            return GraceJSONResult.error();
        }
    }
}

