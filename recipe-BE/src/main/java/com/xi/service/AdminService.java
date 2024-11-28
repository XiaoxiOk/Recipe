package com.xi.service;

import com.xi.model.pojo.Admin;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface AdminService{
    public List<Admin> getAdminList();
    public int judgeAdminInfo(String admName, String admPwd);
    public int getIdByCheckAdmin(String admName, String admPwd);
    public int updateAdminInfo(Admin admin);

}
