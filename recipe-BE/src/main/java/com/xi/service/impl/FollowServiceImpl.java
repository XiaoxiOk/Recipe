package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.mapper.FollowMapper;
import com.xi.model.pojo.Follow;
import com.xi.model.vo.FollowUserVO;
import com.xi.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Slf4j
@Service
public class FollowServiceImpl implements FollowService {

    @Resource
    private FollowMapper followMapper;

    /**
     * 关注对方，并及时更新是否为互关
     * @param fromUid 来自用户ID
     * @param toUid 目标用户ID
     * @return 更新影响条数
     */
    public int doFollow(int fromUid, int toUid){
        Follow follow = new Follow();
        follow.setFromUid(fromUid);
        follow.setToUid(toUid);

        //判断对方是否关注我
        Follow fan = queryFollow(toUid, fromUid);
        if(fan != null){
            follow.setMutual(1);
            fan.setMutual(1);
            followMapper.updateById(fan);
        }else {
            follow.setMutual(0);
        }
        return followMapper.insert(follow);
    }

    //

    /**
     * 查询一条关注记录
     * @param fromUid 来自用户ID
     * @param toUid 目标用户ID
     * @return 一条关注记录
     */
    public Follow queryFollow(int fromUid, int toUid){
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_uid", fromUid);
        queryWrapper.eq("to_uid", toUid);
        List<Follow> fans = followMapper.selectList(queryWrapper);

        Follow fan = null;
        if(fans != null && fans.size() > 0 && !fans.isEmpty()){
            fan = fans.get(0);
        }
        return fan;
    }

    /**
     * 取关用户
     * @param fromUid 来自用户ID
     * @param toUid 目标用户ID
     * @return 影响条数
     */
    public int doCancelFollow(int fromUid, int toUid){
        //判断是否相互关注，如果是，则需要取消双方的关系
        Follow fan = queryFollow(fromUid, toUid);
        if (fan != null && fan.getMutual() == 1) {
            // 抹除双方的朋友关系，自己的关系删除即可
            Follow pendingFan = queryFollow(toUid, fromUid);
            pendingFan.setMutual(0);
            followMapper.updateById(pendingFan);
        }
        return followMapper.deleteById(fan);
    }

    /**
     * 判断我是否关注对方
     * @param myId 我ID
     * @param toUid 对方ID
     * @return true/false
     */
    public boolean judgeDoIFollowUser(int myId, int toUid) {
        Follow follow = queryFollow(myId, toUid);
        return follow != null;
    }

    public IPage<FollowUserVO> getMyFollowsByPage(Page page, int myId){
        return followMapper.getMyFollowsByPage(page, myId);
    }



    public IPage<FollowUserVO> getMyFansByPage(Page page, int myId){
        return followMapper.getMyFansByPage(page, myId);
    }

    @Override
    public int getMyFollowsCounts(int myId) {
        return followMapper.getMyFollowsCounts(myId);
    }

    public int getMyFansCounts(int myId) {
        return followMapper.getMyFansCounts(myId);
    }
}
