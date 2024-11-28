package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.pojo.Follow;
import com.xi.model.vo.FollowUserVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface FollowService{
    public int doFollow(int fromUid, int toUid);
    public int doCancelFollow(int fromUid, int toUid);
    public Follow queryFollow(int fromUid, int toUid);
    public boolean judgeDoIFollowUser(int myId, int toUid);
    public IPage<FollowUserVO> getMyFollowsByPage(Page page, int myId);
    public IPage<FollowUserVO> getMyFansByPage(Page page, int myId);
    public int getMyFollowsCounts(int myId);
    public int getMyFansCounts(int myId);
}
