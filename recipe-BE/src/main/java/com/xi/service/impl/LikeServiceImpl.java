package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xi.common.enums.LikeTypeEnum;
import com.xi.common.utils.BaseInfoProperties;
import com.xi.mapper.LikeMapper;
import com.xi.model.bo.LikeBO;
import com.xi.model.pojo.Like;
import com.xi.service.LikeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Service
public class LikeServiceImpl extends BaseInfoProperties implements LikeService  {

    @Resource
    private LikeMapper likeMapper;

    @Override
    public boolean insertOne(LikeBO likeBO) {
        Like like = new Like();
        BeanUtils.copyProperties(likeBO, like);
        System.out.println("Like:"+like.toString());

        int count = likeMapper.insert(like);
        if( count == 1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int deleteOne(LikeBO likeBO) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", likeBO.getType()).eq("type_id", likeBO.getTypeId()).eq("user_id",likeBO.getUserId());
        return likeMapper.delete(queryWrapper);
    }

    /**
     * 判断是否有点赞关系
     * @param likeBO 点赞对象
     * @return true/false
     */
    public boolean doILike(LikeBO likeBO) {
        String doILike = null;
        // 更新缓存中点赞数、点赞关联关系(+1、添加)
        LikeTypeEnum checkTypeName = LikeTypeEnum.getByCode(likeBO.getType());
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                doILike =  redis.get(REDIS_USER_LIKE_RECIPE + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            case USER_SHARE:
                doILike = redis.get(REDIS_USER_LIKE_USER_SHARE + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            case COMMENT:
                doILike = redis.get(REDIS_USER_LIKE_COMMENT + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            case REPLY:
                doILike = redis.get(REDIS_USER_LIKE_REPLY + ":" + likeBO.getUserId() + ":" + likeBO.getTypeId());
                break;
            default: break;
        }
        boolean isLike = false;
        if (StringUtils.isNotBlank(doILike) && doILike.equalsIgnoreCase("1")) {
            isLike = true;
        }
        return isLike;
    }

    /**
     * 获取总的点赞数
     * @param type 被点赞对象类型
     * @param typeId 被点赞对象ID
     * @return 数量0+
     */
    public Integer getBeLikedCounts(int type, int typeId) {
        String countsStr = null;

        LikeTypeEnum checkTypeName = LikeTypeEnum.getByCode(type);
        switch (Objects.requireNonNull(checkTypeName)) {
            case RECIPE:
                countsStr = redis.get(REDIS_RECIPE_BE_LIKED_COUNTS + ":" + typeId);
                break;
            case USER_SHARE:
                countsStr = redis.get(REDIS_USER_SHARE_BE_LIKED_COUNTS + ":" + typeId);
                break;
            case COMMENT:
                countsStr = redis.get(REDIS_COMMENT_BE_LIKED_COUNTS + ":" + typeId);
                break;
            case REPLY:
                countsStr = redis.get(REDIS_REPLY_BE_LIKED_COUNTS + ":" + typeId);
                break;
            default: break;
        }

        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }

    @Override
    public List<Integer> getRecipesByUserId(int userId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", LikeTypeEnum.RECIPE);
        queryWrapper.eq("user_id", userId);
        return likeMapper.getRecipesByUserId(userId, LikeTypeEnum.RECIPE.getCode());
    }


}
