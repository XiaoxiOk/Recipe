package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.mapper.UserShareMapper;
import com.xi.model.bo.UserShareBO;
import com.xi.model.pojo.UserShare;
import com.xi.model.vo.UserShareVO;
import com.xi.service.UserShareService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Service
public class UserShareServiceImpl implements UserShareService {
    @Resource
    private UserShareMapper userShareMapper;


    @Override
    public IPage<UserShareVO> getMySharesByPage(Page<UserShareVO> page, int myId) {

        return userShareMapper.getMySharesByPage(page, myId);
    }

    public IPage<UserShareVO> getFollowSharesByPage(Page<UserShareVO> page, int fanId) {
        return userShareMapper.queryFollowSharesByPage(page, fanId);
    }

    @Override
    public UserShareVO getUserShareVOById(int id) {
        return userShareMapper.queryUserShareVOById(id);
    }

    @Override
    public UserShare insertUserShare(UserShareBO userShareBO) {
        UserShare userShare = new UserShare();
        BeanUtils.copyProperties(userShareBO, userShare);
        int count = userShareMapper.insert(userShare);
        if(count == 1){
            return userShare; //插入成功后用户id会自动填充!
        }else{
            return null;
        }
    }

    public UserShare getBasicByUId(int id){
        return userShareMapper.selectById(id);
    }
    @Override
    public int updateUserShareById(UserShare userShare) {
        return userShareMapper.updateById(userShare);
    }

    @Override
    public int deleteById(int id) {
        return userShareMapper.deleteById(id);
    }
}
