package com.xi.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.CommentTypeEnum;
import com.xi.common.enums.LikeTypeEnum;
import com.xi.common.enums.RecipeStateEnum;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.model.pojo.*;
import com.xi.model.vo.RecipeVO;
import com.xi.service.AdminService;
import com.xi.service.RecipeService;
import com.xi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Slf4j
@RestController
@Api(tags = "Admin管理")
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;
    @Resource
    private RecipeService recipeService;

    @ApiOperation(value = "登录")
    @PostMapping("doLogin")
    public GraceJSONResult doLogin(@RequestParam("admName") String admName, @RequestParam("admPwd") String admPwd){
        log.info("adminDoLogin:"+admName+":"+admPwd);
        int queryResult = adminService.judgeAdminInfo(admName, admPwd);
        if(queryResult == 0){
            return GraceJSONResult.loginFailedMsg("密码不正确");
        }else if(queryResult == -1){
            return GraceJSONResult.loginFailedMsg("用户名不存在");
        }else{
            StpUtil.login(queryResult);//管理员ID
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();//将token信息返回给前端
            return GraceJSONResult.ok(tokenInfo);
        }
    }

    @ApiOperation(value = "更改用户密码")
    @PostMapping("/updateAdminInfo")
    public GraceJSONResult updateAdminInfo(@RequestParam("oldName") String oldName, @RequestParam("oldPwd") String oldPwd, @RequestParam("newName") String newName, @RequestParam("newPwd") String newPwd){
        Admin oldAdm = new Admin(oldName, oldPwd);
        Admin newAdm = new Admin(newName, newPwd);
        int id=adminService.getIdByCheckAdmin(oldName, oldPwd);
        if(id == -1){
            log.info("匹配不到"+oldAdm.toString());
            return GraceJSONResult.notifyMsg("匹配不到"+oldAdm.toString());
        }else{
            newAdm.setId(id);   // 根据id更改
            if(adminService.updateAdminInfo(newAdm) == 1){
                return GraceJSONResult.ok();
            }else{
                return GraceJSONResult.errorMsg("操作失败！");
            }
        }
    }

    @ApiOperation(value = "管理员获取用户信息")
    @SaCheckLogin
    @PostMapping("/getUserList")
    public GraceJSONResult getUserList(@RequestBody Page<User> page, @RequestParam("type") int type){
        IPage<User> contentIPage = userService.selectUsersByPage(page, type);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "管理员冻结/解冻用户")
    @PostMapping("/freezeUser")
    public Object freezeUser( @RequestParam("userId") int userId, @RequestParam("isFrozen") int isFrozen){
        if(userService.updateUserStateById(userId, isFrozen) == 1){
            return GraceJSONResult.ok("操作成功！");
        }else{
            return GraceJSONResult.errorMsg("操作失败！");
        }
    }

    @ApiOperation(value = "查询登录状态")
    @GetMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

    @ApiOperation(value = "管理员请求退出登录，注销会话")
    @SaCheckLogin
    @GetMapping("logout")
    public SaResult logout() {
        String loginId = null;
        if (StpUtil.isLogin()){
            loginId = (String) StpUtil.getLoginId();
            StpUtil.logout();//退出登录，且自动删除redis缓存信息
        }
        return SaResult.ok("会话ID为 " + loginId + " 的管理员退出登录成功");
    }

    @ApiOperation(value = "管理员获取匹配用户名的用户信息列表（mobile唯一）")
    @SaCheckLogin
    @PostMapping("/getUserInfoByName")
    public GraceJSONResult getUserInfoByName(@RequestBody Page page, @RequestParam("userName") String userName){
        IPage<User> contentIPage = userService.getUserByUserName(page,userName);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }
}

