package com.xi.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.CommentTypeEnum;
import com.xi.common.enums.LikeTypeEnum;
import com.xi.common.enums.MessageTypeEnum;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.model.bo.CommentBO;
import com.xi.model.bo.LikeBO;
import com.xi.model.pojo.Comment;
import com.xi.model.pojo.Message;
import com.xi.model.pojo.Recipe;
import com.xi.model.pojo.UserShare;
import com.xi.model.vo.CommentVO;
import com.xi.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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
@Slf4j
@RestController
@Api(tags = "CommentController")
@RequestMapping("/comment")
public class CommentController extends BaseInfoProperties {

    @Resource
    private CommentService commentService;
    @Resource
    private LikeService likeService;
    @Resource
    private ReplyService replyService;
    @Resource
    private MessageService messageService;
    @Resource
    private RecipeService recipeService;
    @Resource
    private UserShareService userShareService;

    @ApiOperation(value = "获取评论列表-客户端")
    @PostMapping("/getCommentListByPage")
    public GraceJSONResult getCommentListByPage(@RequestBody Page<CommentVO> page, @RequestParam("type") int type, @RequestParam("typeId") int typeId,
                                                @RequestParam("userId") int userId) {
        if(!CommentTypeEnum.checkTypeIsRight(type)){
            return GraceJSONResult.notifyMsg("传入的评论类型不匹配！");
        }

        IPage<CommentVO> contentIPage = commentService.getCommentListByPage(page, type, typeId);

        List<CommentVO> commentVOList = contentIPage.getRecords();
        if(commentVOList.size() <= 0){
            if(contentIPage.getTotal() <= 0){
                return GraceJSONResult.ok("快去抢沙发！");
            }else {
                return GraceJSONResult.notifyMsg("到底了哦！");
            }
        }
        for (CommentVO commentVO: commentVOList) {
            // 当前评论获赞数
            commentVO.setLikedCounts(likeService.getBeLikedCounts(LikeTypeEnum.COMMENT.getCode(), commentVO.getId()));
            // 当前评论被回复数
            commentVO.setRepliedCounts(replyService.getRepliedCounts(commentVO.getId()));
            //判断当前用户是否点赞过该评论
            LikeBO likeBO = new LikeBO();
            likeBO.setType(LikeTypeEnum.COMMENT.getCode());
            likeBO.setUserId(userId);
            likeBO.setTypeId(commentVO.getId());
            commentVO.setDoILikeThis(likeService.doILike(likeBO));

        }
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }


    @ApiOperation(value = "获取评论列表-管理员端")
    @PostMapping("/getCommentsByPage")
    public GraceJSONResult getCommentsByPage(@RequestBody Page<CommentVO> page, @Param("type") int type, @Param("typeId") int typeId) {
        IPage<CommentVO> contentIPage = commentService.getCommentListByPage(page, type, typeId);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }
    @ApiOperation(value = "添加评论")
    @PostMapping("/addComment")
    public GraceJSONResult addComment(@RequestBody Comment comment, @RequestParam("msgToUid") Integer msgToUid) {
        if(!CommentTypeEnum.checkTypeIsRight(comment.getTopicType())){
            return GraceJSONResult.notifyMsg("传入的评论类型不匹配！");
        }

        if(msgToUid == null){
            return GraceJSONResult.notifyMsg("通知目标用户ID不能为空哦！");
        }

        Comment insertComment = commentService.insertComment(comment);
        if(insertComment == null){
            return GraceJSONResult.notifyMsg("添加评论失败！");
        }

        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(insertComment, commentVO);
        // 消息对象：通知关注
        Message message = new Message();
        message.setFromUid(commentVO.getFromUid());
        message.setToUid(msgToUid);
        message.setTypeId(commentVO.getId()); //目标ID是评论ID
        Map<String, Object> content = new HashMap();

        // 更新缓存中点赞数、点赞关联关系(+1、添加)
        CommentTypeEnum checkTypeName = CommentTypeEnum.getByCode(comment.getTopicType());
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                redis.increment(REDIS_RECIPE_COMMENT_COUNTS + ":" + commentVO.getTopicId(), 1);
                message.setType(MessageTypeEnum.COMMENT_RECIPE.type);
                Recipe queryRecipe = recipeService.getRecipeBasicById(commentVO.getTopicId());
                content.put("showImage", queryRecipe.getShowImage());
                content.put("commentContent", commentVO.getContent());
                message.setContent(new JSONObject(content).toString());
                break;
            case USER_SHARE:
                redis.increment(REDIS_USER_SHARE_COMMENT_COUNTS + ":" + commentVO.getTopicId(), 1);
                message.setType(MessageTypeEnum.COMMENT_USER_SHARE.type);
                UserShare queryUserShare = userShareService.getBasicByUId(comment.getTopicId());
                content.put("userShareContent", queryUserShare.getContent());
                content.put("commentContent", commentVO.getContent());
                message.setContent(new JSONObject(content).toString());
                break;
            default: break;
        }

        //消息：通知评论：菜谱/动态
        messageService.insertMessage(message);

        return GraceJSONResult.ok(commentVO);
    }

    @ApiOperation(value = "删除评论--楼主用户/管理员")
    @PostMapping("/deleteComment")
    public GraceJSONResult deleteComment(@RequestBody CommentBO commentBO){
        if(!CommentTypeEnum.checkTypeIsRight(commentBO.getTopicType())){
            return GraceJSONResult.notifyMsg("传入的评论类型不匹配！");
        }

        if(commentService.deleteCommentById(commentBO.getId()) != 1){
            return GraceJSONResult.notifyMsg("删除评论操作失败！");
        }
        // 评论总数的累减
        CommentTypeEnum checkTypeName = CommentTypeEnum.getByCode(commentBO.getTopicType());
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:

                redis.decrement(REDIS_RECIPE_COMMENT_COUNTS + ":" + commentBO.getTopicId(), 1);
                break;
            case USER_SHARE:
                redis.decrement(REDIS_USER_SHARE_COMMENT_COUNTS + ":" + commentBO.getTopicId(), 1);
                break;
            default: break;
        }

        return GraceJSONResult.ok("删除评论成功！");
    }


    @ApiOperation("获取评论（含回复）总数")
    @GetMapping("getCommentCounts")
    public GraceJSONResult getCommentCounts(@RequestParam int type, @RequestParam int typeId){
        if(!CommentTypeEnum.checkTypeIsRight(type)){
            return GraceJSONResult.notifyMsg("传入的评论类型不匹配！");
        }

        Map<String, Integer> count = new HashMap<>();
        count.put("counts",commentService.getCommentCounts(type,typeId));
        return GraceJSONResult.ok(count);
    }
}

