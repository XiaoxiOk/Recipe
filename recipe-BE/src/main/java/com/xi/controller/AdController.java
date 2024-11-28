package com.xi.controller;


import com.xi.common.grace.GraceJSONResult;
import com.xi.model.pojo.Ad;
import com.xi.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@Api(tags = "Ad管理")
@RequestMapping("/ad")
public class AdController {
    @Resource
    private AdService adService;
    @ApiOperation(value = "获取所有广告位信息")
    @GetMapping("/getAllAd")
    public GraceJSONResult getAllAd() {
        return GraceJSONResult.ok(adService.getAllAd());
    }

    @ApiOperation(value = "根据ID获取广告")
    @PostMapping("getAdById")
    public GraceJSONResult getAdById(@RequestParam("adId") int adId) {
        return GraceJSONResult.ok(adService.getAdById(adId));
    }

    @ApiOperation(value = "更换修改位信息")
    @PostMapping("/updateAd")
    public GraceJSONResult updateAd(@RequestBody Ad ad) {
        int count = adService.updateAd(ad);
        if (count == 0) {
            return GraceJSONResult.notifyMsg("更新失败！");
        } else {
            return GraceJSONResult.ok();
        }
    }
}

