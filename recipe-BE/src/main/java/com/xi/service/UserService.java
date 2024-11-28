package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.bo.RegistLoginBO;
import com.xi.model.pojo.User;
import io.swagger.models.auth.In;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface UserService {

    IPage<User> selectUsersByPage(Page<User> page, int isFrozen);
    IPage<User> getUserByUserName(Page<User> page, String userName);
    public User getUserById(int id);
    public User queryMobileIsExist(String mobile);
    public User createUser( RegistLoginBO registLoginBO);
    public int insertOne(User user);
    public int updateUserById(User user);
    public int updateUserNameById(int id, String userName);
    public int updateProfilePhotoById(int id, String profilePhoto);
    public int updateGenderById(int id, int gender);
    public int updateBirthdayById(int id, Date birthday);
    public int updateUserStateById(int id, int isFrozen);
    public int deleteUserById(int id);
}
