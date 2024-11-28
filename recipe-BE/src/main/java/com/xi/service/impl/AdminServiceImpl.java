package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xi.mapper.AdminMapper;
import com.xi.model.pojo.Admin;
import com.xi.service.AdminService;
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
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;
    @Override
    public List<Admin> getAdminList() {
        return adminMapper.selectList(null);
    }

    public int updateUserInfo(Admin admin){return adminMapper.updateById(admin);}

    public int judgeAdminInfo(String admName, String admPwd){
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<Admin>();
        adminQueryWrapper.eq("adm_name", admName).eq("adm_pwd",admPwd);
        //获得Admin的name&pwd一致的个数
        Admin adminResult = adminMapper.selectOne(adminQueryWrapper);
        if(adminResult != null){
            return adminResult.getId(); //正确可登录并返回id
        }else if(adminMapper.selectCount(new QueryWrapper<Admin>().eq("adm_name",admName))!= 0){
            return 0; //密码不正确
        }else{
            return -1;//用户名不存在
        }

    }

    public int getIdByCheckAdmin(String admName, String admPwd){
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<Admin>();
        adminQueryWrapper.select("id").eq("adm_name", admName).eq("adm_pwd",admPwd);
        List<Admin> adminList = adminMapper.selectList(adminQueryWrapper);
        if(adminList.size()==1){
            return adminList.get(0).getId();
        }else{
            return -1;
        }
    }

    public int updateAdminInfo(Admin admin){
        return adminMapper.updateById(admin);
    }
}
