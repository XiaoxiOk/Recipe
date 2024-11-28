package com.xi.controller;

import com.xi.common.grace.GraceJSONResult;
import com.xi.common.utils.SaUserCheckLogin;
import com.xi.model.pojo.Rate;
import com.xi.service.RateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wml
 * @since 2023-05-14
 */
@Slf4j
@RestController
@Api(tags = "RateController")
@RequestMapping("/rate")
public class RateController {
    @Resource
    private RateService rateService;

    @ApiOperation(value = "用户添加评分")
    @PostMapping("/addRate")
    public GraceJSONResult addRate(@RequestBody Rate rate){
       Rate resultRate = rateService.addRate(rate);
       if(resultRate!=null){
           Map<String, Integer> result = new HashMap<>();
           result.put("rateId", resultRate.getId());
           result.put("rateValue", resultRate.getScore());
           return GraceJSONResult.ok(result);
       }else{
           return GraceJSONResult.errorMsg("操作失败！");
       }

    }

    @ApiOperation(value = "用户修改评分")
    @PostMapping("/updateRateById")
    public GraceJSONResult updateRateById(@RequestParam("rateId") int rateId, @RequestParam("score") int score){
        if(rateService.updateRate(rateId, score) == 1){
            return GraceJSONResult.ok("操作成功！");
        }else{
            return GraceJSONResult.errorMsg("操作失败！");
        }
    }
}
