package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.mapper.UserMapper;
import com.xi.model.bo.RegistLoginBO;
import com.xi.model.pojo.User;
import com.xi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;


    /**
     * 查询是否有mobile对应的用户信息
     * @param mobile 用户手机号
     * @return 用户信息，无则为null
     */
    public User queryMobileIsExist(String mobile){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile",mobile);
        return userMapper.selectOne(userQueryWrapper);
    }
    /**
     * 根据类型，分页获取用户信息列表
     * @param page 分页对象
     * @param isFrozen 类型 1:已冻结，0正常，-1全部，
     * @return 根据类型筛选后的用户信息列表
     */
    @Override
    public IPage<User> selectUsersByPage(Page<User> page, int isFrozen) {
        return userMapper.selectUsersByIsFrozen(page, isFrozen);
    }

    /**
     * 根据用户ID获取一条用户信息
     * @param id 用户ID
     * @return 用户信息
     */

    @Override
    public User getUserById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 管理员输入用户名，匹配检索列表
     * @param page 页面对象
     * @param userName 输入的用户名
     * @return 分页用户信息列表
     */
    @Override
    public IPage<User> getUserByUserName(Page<User> page, String userName){
        return userMapper.selectUsersByName(page, userName);
    }

    /**
     * 创建用户信息，并返回
     * @param registLoginBO 接收的用户信息
     * @return 在数据库中创建成功的用户信息
     */
    @Override
    public User createUser( RegistLoginBO registLoginBO){
        User newUser = new User();
        BeanUtils.copyProperties(registLoginBO, newUser);
        newUser.setFrozen(0);

        int count = userMapper.insert(newUser);

        if(count == 1){
            return newUser; //插入成功后用户id会自动填充!
        }else{
            return null;
        }
    }
    /**
     * 新增一条用户信息
     * @Parm User 待入库用户信息
     * @return 影响条数
     */
    @Override
    public int insertOne(User user) {
        return userMapper.insert(user);
    }

    /**
     * 更新用户信息
     * @param user 目标用户信息
     * @return 影响条数
     */
    @Override
    public int updateUserById(User user) {
        return userMapper.updateById(user);
    }

    /**
     * 获取指定用户名个数
     * @param userName 用户名
     * @return 个数
     */
    public long getUserNameCount(String userName){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",userName);
        return userMapper.selectCount(queryWrapper);
    }
    /**
     * 根据id更新用户名，需要校验是否已有用户名
     * @param id 用户id
     * @param userName 目标用户名
     * @return 影响条数，-1表示已有该用户昵称（已核实微信昵称是惟一的）
     */
    @Override
    public int updateUserNameById(int id, String userName) {

        if(getUserNameCount(userName)!=0){
            return -1;//已有用户名，不支持修改
        }
        User user = new User();
        user.setUserName(userName);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        return userMapper.update(user, updateWrapper);
    }


    /**
     * 修改用户头像
     * @param id 用户ID
     * @param profilePhoto 用户头像
     * @return 影响条数
     */
    @Override
    public int updateProfilePhotoById(int id, String profilePhoto) {
        //TODO 实现用户头像上传！！
        return 0;
    }

    /**
     * 根据id更新用户性别信息
     * @param id 用户id
     * @param gender 用户性别0未选择，1男，2女
     * @return 影响条数
     */
    @Override
    public int updateGenderById(int id, int gender) {
        User user = new User();
        user.setGender(gender);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        return userMapper.update(user, updateWrapper);
    }

    /**
     * 根据id修改用户生日信息
     * @param id 用户id
     * @param birthday 目标生日
     * @return 影响条数
     */
    @Override
    public int updateBirthdayById(int id, Date birthday) {
        User user = new User();
        user.setBirthday(birthday);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        return userMapper.update(user, updateWrapper);
    }

    /**
     * 根据用户ID更改用户冻结状态
     * update(T t,Wrapper updateWrapper)时t不能为空,否则自动填充失效
     * @param id 用户ID
     * @param isFrozen 1表示冻结，0正常可登录
     * @return 更新条数
     */
    @Override
    public int updateUserStateById(int id, int isFrozen) {
        User user = new User();
        user.setFrozen(isFrozen);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        return userMapper.update(user, updateWrapper); //不会覆盖只更新需要的内容
    }

    /**
     * 根据用户ID删除用户信息
     * @param id 用户ID
     * @return 影响条数
     */
    @Override
    public int deleteUserById(int id) {
        return userMapper.deleteById(id);
    }
}
