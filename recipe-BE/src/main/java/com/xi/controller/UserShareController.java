package com.xi.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.CommentTypeEnum;
import com.xi.common.enums.LikeTypeEnum;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.model.bo.LikeBO;
import com.xi.model.bo.UserShareBO;
import com.xi.model.pojo.*;
import com.xi.model.vo.UserShareVO;
import com.xi.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
@RequestMapping("/user-share")
@Api(tags = "UserShareController")
public class UserShareController {

    @Resource
    private UserShareService userShareService;

    @Resource
    private MediaService mediaService;

    @Resource
    private LikeService likeService;

    @Resource
    private CommentService commentService;

    @Resource
    private FollowService followService;

    @ApiOperation(value = "分页获取自己的全部动态信息")
    @PostMapping("/getShareListByMyId")
    public GraceJSONResult getShareListByMyId(@RequestBody Page<UserShareVO> page, @RequestParam("myId") int myId){

        IPage<UserShareVO> userShareVOIPage =  userShareService.getMySharesByPage(page, myId);
        //fanId小于0时，表示全部用户动态，大于0则表示获取关注者的动态信息

        List<UserShareVO> userShareList = userShareVOIPage.getRecords();
        for (UserShareVO userShareVO: userShareList) {
            int userShareId = userShareVO.getId();
            List<Media> mediaList = mediaService.getMediaListByShareId(userShareId);
            userShareVO.setMediaList(mediaList);//获取每个动态的图片信息
            //点赞总数和评论总数
            userShareVO.setLikedCounts(likeService.getBeLikedCounts(LikeTypeEnum.USER_SHARE.getCode(), userShareId));
            userShareVO.setCommentCounts(commentService.getCommentCounts(CommentTypeEnum.USER_SHARE.getCode(), userShareId));
        }
        return DuplicateMethod.getDataListByPage(userShareVOIPage, page);
    }
    
    
    @ApiOperation(value = "分页获取用户动态全部信息列表")
    @PostMapping("/getUserShareAllByPage")
    public GraceJSONResult getUserShareAllByPage(@RequestBody Page<UserShareVO> page, @RequestParam("fanId") int fanId){

        IPage<UserShareVO> userShareVOIPage = null;
        //fanId小于0时，表示全部用户动态，大于0则表示获取关注者的动态信息
        if(fanId <= 0){
            userShareVOIPage = userShareService.getFollowSharesByPage(page, -1);
        }else{
            userShareVOIPage = userShareService.getFollowSharesByPage(page, fanId);

        }

        List<UserShareVO> userShareList = userShareVOIPage.getRecords();
        for (UserShareVO userShareVO: userShareList) {
            int userShareId = userShareVO.getId();
            List<Media> mediaList = mediaService.getMediaListByShareId(userShareId);
            userShareVO.setMediaList(mediaList);//获取每个动态的图片信息
            //点赞总数和评论总数
            userShareVO.setLikedCounts(likeService.getBeLikedCounts(LikeTypeEnum.USER_SHARE.getCode(), userShareId));
            userShareVO.setCommentCounts(commentService.getCommentCounts(CommentTypeEnum.USER_SHARE.getCode(), userShareId));
        }
        return DuplicateMethod.getDataListByPage(userShareVOIPage, page);
    }

    @ApiOperation(value = "根据ID获取用户动态全部信息")
    @GetMapping("/getUserShareWholeById")
    public GraceJSONResult getUserShareWholeById(@RequestParam("userShareId") int userShareId, @RequestParam("myId") int myId){

        UserShareVO userShareVO = userShareService.getUserShareVOById(userShareId);
        if(userShareVO == null){
            return GraceJSONResult.notifyMsg("不存在该编号为["+userShareId+"]的用户动态信息");
        }
        List<Media> mediaList = mediaService.getMediaListByShareId(userShareId);
        userShareVO.setMediaList(mediaList);
        //判断菜谱是否被"我"点赞信息
        LikeBO likeBO = new LikeBO();
        likeBO.setUserId(myId);
        likeBO.setTypeId(userShareId);
        likeBO.setType(LikeTypeEnum.USER_SHARE.getCode());
        userShareVO.setDoILikeThis(likeService.doILike(likeBO));

        //判断"我"是否关注发布该菜谱的作者
        userShareVO.setDoIFollowWriter(followService.judgeDoIFollowUser(myId, userShareVO.getUid()));

        //获取总的点赞数和评论数
        userShareVO.setLikedCounts(likeService.getBeLikedCounts(LikeTypeEnum.USER_SHARE.getCode(), userShareId));
        userShareVO.setCommentCounts(commentService.getCommentCounts(CommentTypeEnum.USER_SHARE.getCode(), userShareId));

        return GraceJSONResult.ok(userShareVO);
    }

    @ApiOperation(value = "新增整个用户动态信息")
    @PostMapping("/addUserShareWhole")
    public GraceJSONResult addUserShareWhole(@RequestBody UserShareBO userShareBO){
        UserShare userShareResult = userShareService.insertUserShare(userShareBO);
        if(userShareResult == null){
            GraceJSONResult.errorMsg("用户动态基本信息添加失败！");
        }
        List<Media> mediaList = userShareBO.getMediaList();
        if(mediaList.size()>0){
            //为每个图片信息绑定新增成功的动态编号
            for (Media image : mediaList) {
                image.setUserShareId(userShareResult.getId());
            }
            if(mediaService.insertOrUpdateMediaList(mediaList)==0 && mediaList.size()!=0){
                return GraceJSONResult.errorMsg("图片信息添加失败！");
            }

        }

        return GraceJSONResult.ok("用户动态信息添加成功！");
    }



    @ApiOperation(value = "更新整个用户动态信息")
    @PostMapping("/updateUserShareWhole")
    public GraceJSONResult updateUserShareWhole(@RequestBody UserShareBO userShareBO, @RequestParam(value = "mediaIdsToDel",required = false) List<Integer> mediaIdsToDel){
        //返回1表示插入成功；-1表示插入后的已存在，拒绝插入；0表示插入数据库操作失败
        int count;
        UserShare userShare = new UserShare();
        BeanUtils.copyProperties(userShareBO, userShare);
        count = userShareService.updateUserShareById(userShare);//修改基本信息
        log.info("用户动态基本信息修改影响条数："+count);
        List<Media> mediaList = userShareBO.getMediaList();
        for (Media media : mediaList) {
            media.setUserShareId(userShare.getId());
        }
        count = mediaService.insertOrUpdateMediaList(mediaList);//更新图片信息：修改+新增未有的
        log.info("批量更新图片信息影响条数："+count);
        if(mediaIdsToDel != null && mediaIdsToDel.size()!= 0){
            count = mediaService.delMediaByIds(mediaIdsToDel);
            log.info("批量删除图片信息影响条数："+count);
        }
        
        return GraceJSONResult.ok("用户动态全部信息更新成功！");
    }

    @ApiOperation(value = "根据id删除用户动态信息")
    @PostMapping("/deleteUserShareWhole")
    public GraceJSONResult deleteUserShareWhole(@RequestParam("userShareId") int userShareId){
        int count = userShareService.deleteById(userShareId);
        log.info("删除基本信息条数："+ count );
        mediaService.deleteByUserShareId(userShareId); //数据路库设置了级联删除
        return GraceJSONResult.ok();
    }
}

