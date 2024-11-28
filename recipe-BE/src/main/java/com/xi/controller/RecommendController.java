package com.xi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.RecipeStateEnum;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.model.pojo.Recipe;
import com.xi.model.vo.RecipeShowVO;
import com.xi.service.RateService;
import com.xi.service.RecipeService;
import com.xi.service.RecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/recommend")
@Api(tags = "RecommendController")
public class RecommendController {
    @Resource
    private RecipeService recipeService;
    @Resource
    private RecommendService recommendService;
    @Resource
    private RateService rateService;

    @ApiOperation(value = "根据当前用户ID获取推荐菜谱基本信息")
    @PostMapping("/getRecommendListByUserId")
    public GraceJSONResult getRecommendListByUserId(@RequestParam("userId") int userId){
        if(rateService.isNotRateRecipe(userId)){
            Map<String, Object> result = new HashMap<>();
            result.put("msg","还没有评过分哦！");
            result.put("count",0);
            return GraceJSONResult.ok(result);
        }
        List<Integer> recommendations = recommendService.userCfRecommend(userId,3);
       log.info("为userId=【"+userId+"】的用户个性化推荐菜谱ID："+recommendations.toString());

        List<Recipe> recipes = recipeService.selectByIdList(recommendations);
        Map<String, Object> result = new HashMap<>();
        result.put("records", recipes);
        result.put("ids", recommendations);
        return GraceJSONResult.ok(result);
    }

}
