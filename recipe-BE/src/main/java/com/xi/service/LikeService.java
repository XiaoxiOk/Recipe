package com.xi.service;

import com.xi.model.bo.LikeBO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface LikeService{
    boolean insertOne(LikeBO likeBO);
    int deleteOne(LikeBO likeBO);
    public boolean doILike(LikeBO likeBO);
    public Integer getBeLikedCounts(int type, int typeId);
    public List<Integer> getRecipesByUserId(int userId);
}
