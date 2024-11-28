package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.bo.UserShareBO;
import com.xi.model.pojo.UserShare;
import com.xi.model.vo.UserShareVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface UserShareService{
    IPage<UserShareVO> getMySharesByPage(Page<UserShareVO> page, int myId);
    public IPage<UserShareVO> getFollowSharesByPage(Page<UserShareVO> page, int fanId);
    public UserShareVO getUserShareVOById(int id);
    public UserShare getBasicByUId(int id);
    public UserShare insertUserShare(UserShareBO userShareBO);
    public int updateUserShareById(UserShare userShare);
    public int deleteById(int id);
}
