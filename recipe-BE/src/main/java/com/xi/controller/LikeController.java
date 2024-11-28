package com.xi.controller;


import com.alibaba.fastjson.JSONObject;
import com.xi.common.enums.LikeTypeEnum;
import com.xi.common.enums.MessageTypeEnum;
import com.xi.common.grace.GraceJSONResult;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.model.bo.LikeBO;
import com.xi.model.pojo.*;
import com.xi.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Slf4j
@RestController
@RequestMapping("/like")
@Api(tags = "LikeController")
public class LikeController extends BaseInfoProperties {
    @Resource
    private LikeService likeService;

    @Resource
    private MessageService messageService;

    @Resource
    private RecipeService recipeService;
    @Resource
    private UserShareService userShareService;
    @Resource
    private CommentService commentService;
    @Resource
    private ReplyService replyService;

    @ApiOperation(value = "点赞")
    @PostMapping("/doLike")
    public GraceJSONResult doLike(@RequestBody LikeBO likeBO, @RequestParam("msgToUid") Integer msgToUid) {


        if(!LikeTypeEnum.checkLikeTypeIsRight(likeBO.getType())){
            return GraceJSONResult.notifyMsg("传入的点赞类型不匹配！");
        }

        if(msgToUid == null){
            return GraceJSONResult.notifyMsg("通知目标用户ID不能为空哦！");
        }

        // 我点赞的内容-关联关系保存到数据库

        if (!likeService.insertOne(likeBO)) {
            return GraceJSONResult.notifyMsg("点赞失败！");
        }

        // 消息对象：通知点赞
        Message message = new Message();
        message.setFromUid(likeBO.getUserId());
        message.setToUid(msgToUid);
        message.setTypeId(likeBO.getTypeId());
        Map<String, Object> content = new HashMap();
        // 更新缓存中点赞数、点赞关联关系(+1、添加)
        LikeTypeEnum checkTypeName = LikeTypeEnum.getByCode(likeBO.getType());
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                redis.increment(REDIS_RECIPE_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.set(REDIS_USER_LIKE_RECIPE + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId(), "1");

                message.setType(MessageTypeEnum.LIKE_RECIPE.type);
                Recipe queryRecipe = recipeService.getRecipeBasicById(likeBO.getTypeId());
                content.put("recipeName", queryRecipe.getRecipeName());
                content.put("showImage", queryRecipe.getShowImage());
                message.setContent(new JSONObject(content).toString());
                break;
            case USER_SHARE:
                redis.increment(REDIS_USER_SHARE_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.set(REDIS_USER_LIKE_USER_SHARE + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId(), "1");
                message.setType(MessageTypeEnum.LIKE_USER_SHARE.type);

                UserShare queryUserShare = userShareService.getBasicByUId(likeBO.getTypeId());
                content.put("userSharContent", queryUserShare.getContent());
                message.setContent(new JSONObject(content).toString());
                break;
            case COMMENT:
                redis.increment(REDIS_COMMENT_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.set(REDIS_USER_LIKE_COMMENT + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId(), "1");
                message.setType(MessageTypeEnum.LIKE_COMMENT.type);
                Comment queryComment = commentService.getCommentById(likeBO.getTypeId());
                content.put("commentType", queryComment.getTopicType());
                content.put("commentContent", queryComment.getContent());
                message.setContent(new JSONObject(content).toString());
                break;
            case REPLY:
                redis.increment(REDIS_REPLY_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.set(REDIS_USER_LIKE_REPLY + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId(), "1");
                message.setType(MessageTypeEnum.LIKE_REPLY.type);
                Reply queryReply = replyService.getReplyById(likeBO.getTypeId());
                content.put("replyContent", queryReply.getContent());
                message.setContent(new JSONObject(content).toString());
                break;
            default: break;
        }

        //消息：通知点赞
        messageService.insertMessage(message);

        // 点赞完毕，获得当前在redis中的总数
        // 比如获得总计数为 1k/1w/10w，假定阈值（配置）为2000
        // 此时1k满足2000，则触发入库

//        String countsStr = redis.get(REDIS_RECIPE_BE_LIKED_COUNTS + ":" + recipeId);
//        log.info("======" + REDIS_RECIPE_BE_LIKED_COUNTS + ":" + recipeId + "======");
//        Integer counts = 0;
//        if (StringUtils.isNotBlank(countsStr)) {
//            counts = Integer.valueOf(countsStr);
//            if (counts >= nacosCounts) {
//                recipeService.flushCounts(recipeId, counts);
//            }
//        }

        return GraceJSONResult.ok();
    }


    @ApiOperation(value = "取消点赞")
    @PostMapping("unlike")
    public GraceJSONResult unlike(@RequestBody LikeBO likeBO) {

        if(!LikeTypeEnum.checkLikeTypeIsRight(likeBO.getType())){
            return GraceJSONResult.notifyMsg("传入的点赞类型不匹配！");
        }

        // 我点赞的内容-关联关系保存到数据库
        int count = likeService.deleteOne(likeBO);
        log.info("deleteOne-count:" + count);
        if (count <= 0) {
            return GraceJSONResult.notifyMsg("取消点赞失败");
        }

        // 更新缓存中点赞数、点赞关联关系(-1、删除)
        LikeTypeEnum checkTypeName = LikeTypeEnum.getByCode(likeBO.getType());
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                redis.decrement(REDIS_RECIPE_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.del(REDIS_USER_LIKE_RECIPE + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            case USER_SHARE:
                redis.decrement(REDIS_USER_SHARE_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.del(REDIS_USER_LIKE_USER_SHARE + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            case COMMENT:
                redis.decrement(REDIS_COMMENT_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.del(REDIS_USER_LIKE_COMMENT + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            case REPLY:
                redis.decrement(REDIS_REPLY_BE_LIKED_COUNTS + ":" + likeBO.getTypeId(), 1);
                redis.del(REDIS_USER_LIKE_REPLY + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            default: break;
        }

        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "获取指定类型的点赞数量")
    @PostMapping("/getBeLikedCounts")
    public GraceJSONResult getBeLikedCounts(@RequestParam("type") int type, @RequestParam("typeId") int typeId){
        return GraceJSONResult.ok(likeService.getBeLikedCounts(type, typeId));
    }
}

