package com.xi.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.CommentTypeEnum;
import com.xi.common.enums.LikeTypeEnum;
import com.xi.common.enums.MessageTypeEnum;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.model.bo.LikeBO;
import com.xi.model.bo.ReplyBO;
import com.xi.model.pojo.Comment;
import com.xi.model.pojo.Message;
import com.xi.model.pojo.Reply;
import com.xi.model.vo.ReplyVO;
import com.xi.service.CommentService;
import com.xi.service.LikeService;
import com.xi.service.MessageService;
import com.xi.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@RestController
@RequestMapping("/reply")
public class ReplyController extends BaseInfoProperties {

    @Resource
    private ReplyService replyService;
    @Resource
    private CommentService commentService;
    @Resource
    private LikeService likeService;
    @Resource
    private MessageService messageService;


    @ApiOperation(value = "获取回复列表--用户端")
    @PostMapping("/getReplyListByPage")
    public GraceJSONResult getReplyListByPage(@RequestBody Page<ReplyVO> page, @RequestParam("commentId") int commentId, @RequestParam("userId") int userId) {

        IPage<ReplyVO> contentIPage = replyService.getReplyListByPage(page, commentId);

        List<ReplyVO> replyVOList = contentIPage.getRecords();
        if(replyVOList.size() <= 0){
            return GraceJSONResult.notifyMsg("暂无回复！");
        }
        for (ReplyVO replyVO : replyVOList) {
            // 当前回复获赞数
            replyVO.setLikedCounts(likeService.getBeLikedCounts(LikeTypeEnum.REPLY.getCode(), replyVO.getId()));
            // 判断当前用户是否点赞过该回复
            LikeBO likeBO = new LikeBO();
            likeBO.setType(LikeTypeEnum.REPLY.getCode());
            likeBO.setUserId(userId);
            likeBO.setTypeId(replyVO.getId());
            replyVO.setDoILikeThis(likeService.doILike(likeBO));
        }
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }


    @ApiOperation(value = "获取回复列表--管理员端")
    @PostMapping("/getRepliesByPage")
    public GraceJSONResult getRepliesByPage(@RequestBody Page<ReplyVO> page, @RequestParam("commentId") int commentId) {
        IPage<ReplyVO> contentIPage = replyService.getReplyListByPage(page, commentId);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "添加回复")
    @PostMapping("/addReply")
    public GraceJSONResult addReply(@RequestBody Reply reply, @RequestParam("type") int type, @RequestParam("typeId") int typeId) {
        if(!CommentTypeEnum.checkTypeIsRight(type)){
            return GraceJSONResult.notifyMsg("传入的回复类型不匹配！");
        }

        Reply insertReply = replyService.insertReply(reply);
        if(insertReply == null){
            return GraceJSONResult.notifyMsg("添加回复失败！");
        }
        ReplyVO replyVO = new ReplyVO();
        BeanUtils.copyProperties(insertReply, replyVO);

        // 更新缓存中点赞数、点赞关联关系(+1、添加)
        CommentTypeEnum checkTypeName = CommentTypeEnum.getByCode(type);
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                redis.increment(REDIS_RECIPE_COMMENT_COUNTS + ":" + typeId, 1);
                break;
            case USER_SHARE:
                redis.increment(REDIS_USER_SHARE_COMMENT_COUNTS + ":" + typeId, 1);
                break;
            default: break;
        }
        redis.increment(REDIS_COMMENT_BE_REPLIED_COUNTS + ":" + reply.getCommentId(),1);
        // 消息：通知回复
        Message message = new Message();
        message.setFromUid(reply.getFromUid());
        message.setToUid(replyVO.getToUid());
        message.setTypeId(replyVO.getId()); //注意对应回复ID
        message.setType(MessageTypeEnum.REPLY_YOU.type);
        Map<String, Object> content = new HashMap<>();
        content.put("replyContent",replyVO.getContent());
        Comment comment = commentService.getCommentById(replyVO.getCommentId());
        content.put("commentContent", comment.getContent());
        message.setContent(new JSONObject(content).toString());
        messageService.insertMessage(message);

        return GraceJSONResult.ok(replyVO);
    }


    @ApiOperation(value = "删除回复-楼主用户/管理员")
    @GetMapping("/deleteReply")
    public GraceJSONResult deleteReply( @RequestParam("replyId") int replyId, @RequestParam("commentId") int commentId){
        Comment comment = commentService.getCommentById(commentId);
        if(comment == null ){ // Comment找不到时为null
            return GraceJSONResult.notifyMsg("检查评论编号是否正确！");
        }

        int count = replyService.deleteReplyById(replyId);
        if(count != 1){
            return GraceJSONResult.notifyMsg("删除回复操作失败！");
        }


        // 评论总数的累减
        CommentTypeEnum checkTypeName = CommentTypeEnum.getByCode(comment.getTopicType());
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                redis.decrement(REDIS_RECIPE_COMMENT_COUNTS + ":" + comment.getTopicId(), 1);
                break;
            case USER_SHARE:
                redis.decrement(REDIS_USER_SHARE_COMMENT_COUNTS + ":" + comment.getTopicId(), 1);
                break;
            default: break;
        }
        redis.decrement(REDIS_COMMENT_BE_REPLIED_COUNTS + ":" + commentId,1);

        return GraceJSONResult.ok("删除回复成功！");
    }

    @ApiOperation("获取指定评论的回复数")
    @PostMapping
    public GraceJSONResult getRepliedCounts(@RequestParam("commentId") int commentId){
        return GraceJSONResult.ok(replyService.getRepliedCounts(commentId));
    }
}

