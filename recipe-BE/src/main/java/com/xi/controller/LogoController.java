package com.xi.controller;


import com.xi.common.grace.GraceJSONResult;
import com.xi.model.pojo.Logo;
import com.xi.service.LogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@RestController
@RequestMapping("/logo")
public class LogoController {

    @Resource
    private LogoService logoService;

    @GetMapping("/getCurrentLogo")
    public GraceJSONResult getCurrentLogo() {
        return GraceJSONResult.ok(logoService.getCurrentLogo());
    }

    @GetMapping("/getAllLogo")
    public GraceJSONResult getAllLogo() {
        return GraceJSONResult.ok(logoService.getAllLogo());
    }

    @GetMapping("/getHistoryLogo")
    public GraceJSONResult getHistoryLogo() {
        return GraceJSONResult.ok(logoService.getHistoryLogo());
    }

    @PostMapping("/updateCurrentLogo")
    public GraceJSONResult updateCurrentLogo(@RequestParam("currentId") int currentId, @RequestBody Logo logo) {
        int logoId = logoService.updateCurrentLogo(currentId, logo);
        if (logoId == -1) {
            return GraceJSONResult.notifyMsg("插入失败");
        } else {
            return GraceJSONResult.ok(logoId);
        }
    }

}

