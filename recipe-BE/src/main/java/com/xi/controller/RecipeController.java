package com.xi.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.CommentTypeEnum;
import com.xi.common.enums.LikeTypeEnum;
import com.xi.common.enums.RecipeStateEnum;
import com.xi.common.grace.DuplicateMethod;
import com.xi.common.grace.GraceJSONResult;
import com.xi.model.bo.LikeBO;
import com.xi.model.bo.RecipeBO;
import com.xi.model.bo.RecipeToUpdateBO;
import com.xi.model.pojo.*;
import com.xi.model.pojo.Recipe;
import com.xi.model.vo.RecipeShowVO;
import com.xi.model.vo.RecipeVO;
import com.xi.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/recipe")
@Api(tags = "RecipeController")
public class RecipeController {
    @Resource
    private RecipeService recipeService;

    @Resource
    private StepService stepService;

    @Resource
    private MaterialService materialService;

    @Resource
    private LikeService likeService;

    @Resource
    private CommentService commentService;

    @Resource
    private FollowService followService;

    @Resource
    private RateService rateService;


    @ApiOperation(value = "分页获取菜谱基本信息列表--客户端")
    @PostMapping("/getRecipeFilterByPage")
    public GraceJSONResult getRecipeFilterPage(@RequestBody Page<Recipe> page, @RequestParam("recommendRecipeIds") List<Integer> recommendRecipeIds){

        IPage<Recipe> contentIPage = recipeService.getRecipeFilterByPage(page, RecipeStateEnum.ON_SHOW.getState(), recommendRecipeIds);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }
    @ApiOperation(value = "分页获取菜谱基本信息列表--客户端")
    @PostMapping("/getRecipeBasicByPage")
    public GraceJSONResult getRecipeBasicByPage(@RequestBody Page<Recipe> page){
        IPage<Recipe> contentIPage = recipeService.getRecipeBasicByPage(page, RecipeStateEnum.ON_SHOW.getState());
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "分页获取菜谱基本信息列表--管理员端")
    @PostMapping("/getRecipesByPageAndState")
    public GraceJSONResult getRecipesByPageAndState(@RequestBody Page<Recipe> page, @RequestParam("state") int state){
        IPage<Recipe> contentIPage = recipeService.getRecipeBasicByPage(page, state);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "分页获取菜谱全部信息列表")
    @PostMapping("/getRecipeAllByPage")
    public GraceJSONResult getRecipeAllByPage(@RequestBody Page<RecipeVO> page, @RequestParam("state") int state){
        //用户只能查看未被下架商品
        IPage<RecipeVO> recipeVOIPage = recipeService.getRecipeVOByPage(page,state);

        List<RecipeVO> recipeVOList = recipeVOIPage.getRecords();
        for (RecipeVO recipeVO: recipeVOList) {

            int recipeId = recipeVO.getId();
            List<Step> steps = stepService.getStepsByRecipeId(recipeId);
            recipeVO.setSteps(steps);//获取每个菜品的步骤信息
            List<Material> materials = materialService.getMaterialsByRecipeId(recipeId);
            recipeVO.setMaterials(materials);//获取每个菜品的食材用料信息
            //获取总的点赞数和评论数
            recipeVO.setLikedCounts(likeService.getBeLikedCounts(LikeTypeEnum.RECIPE.getCode(), recipeId));
            recipeVO.setCommentCounts(commentService.getCommentCounts(CommentTypeEnum.RECIPE.getCode(), recipeId));
        }
        return DuplicateMethod.getDataListByPage(recipeVOIPage, page);
    }


    @ApiOperation(value = "根据ID获取菜谱基本信息")
    @PostMapping("/getRecipeBasicById")
    public GraceJSONResult getRecipeBasicById(@RequestParam("recipeId") int recipeId){
        Recipe recipeBasic = recipeService.getRecipeBasicById(recipeId);
        return GraceJSONResult.ok(recipeBasic);
    }

    @ApiOperation(value = "根据ID获取菜谱用料和步骤信息--管理员端")
    @PostMapping("/getRecipeAppendById")
    public GraceJSONResult getRecipeAppendById(@RequestParam("recipeId") int recipeId){
        Map<String, Object> recipeAppendInfo = new HashMap<>();
        List<Step> steps = stepService.getStepsByRecipeId(recipeId);
        recipeAppendInfo.put("steps",steps);//获取其步骤信息

        List<Material> materials = materialService.getMaterialsByRecipeId(recipeId);
        recipeAppendInfo.put("materials",materials);//获取其步骤信息
        return GraceJSONResult.ok(recipeAppendInfo);
    }

    @ApiOperation(value = "根据ID获取菜谱全部基本信息--用户端修改")
    @GetMapping("/getRecipeToUpdateById")
    public GraceJSONResult getRecipeToUpdateById(@RequestParam("recipeId") int recipeId){

        RecipeVO recipeVO = recipeService.getRecipeUserById(recipeId);
        if(recipeVO == null){
            return GraceJSONResult.notifyMsg("不存在该编号为["+recipeId+"]的菜谱信息");
        }

        List<Step> steps = stepService.getStepsByRecipeId(recipeId);
        recipeVO.setSteps(steps);
        List<Material> materials = materialService.getMaterialsByRecipeId(recipeId);
        recipeVO.setMaterials(materials);

        return GraceJSONResult.ok(recipeVO);
    }

    @ApiOperation(value = "根据菜谱名/状态/一起匹配检索--管理员")
    @PostMapping("/searchRecipeByNameOrState")
    public GraceJSONResult searchRecipeByNameOrState(@RequestBody Page<Recipe> page, @RequestParam("inputStr") String inputStr, @RequestParam("state") int state){
        IPage<Recipe> contentIPage = recipeService.selectRecipeByNameOrState(page, inputStr, state);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "根据菜谱名/二级类别标签匹配检索--用户")
    @PostMapping("/searchRecipeByNameOrSecId")
    public GraceJSONResult searchRecipeByNameOrSecId(@RequestBody Page<RecipeShowVO> page, @RequestParam("recipeName") String recipeName,@RequestParam("secId") int secId){
        log.info("recipeName:",recipeName);
        IPage<RecipeShowVO> contentIPage = recipeService.showRecipeByNameOrSecId(page, recipeName, secId);

        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "根据ID获取菜谱全部信息--用户端")
    @GetMapping("/getRecipeWholeById")
    public GraceJSONResult getRecipeWholeById(@RequestParam("recipeId") int recipeId, @RequestParam("myId") int myId){

        RecipeVO recipeVO = recipeService.getRecipeUserById(recipeId);
        if(recipeVO == null){
            return GraceJSONResult.notifyMsg("不存在该编号为["+recipeId+"]的菜谱信息");
        }

        List<Step> steps = stepService.getStepsByRecipeId(recipeId);
        recipeVO.setSteps(steps);
        List<Material> materials = materialService.getMaterialsByRecipeId(recipeId);
        recipeVO.setMaterials(materials);

        //判断菜谱是否被"我"点赞信息
        LikeBO likeBO = new LikeBO();
        likeBO.setType(LikeTypeEnum.RECIPE.getCode());
        likeBO.setUserId(myId);
        likeBO.setTypeId(recipeId);
        recipeVO.setDoILikeThis(likeService.doILike(likeBO));

        // 获取"我"对该用户的评分
        Rate rate = rateService.findRate(myId, recipeId);
        if(rate != null){
            recipeVO.setRateId(rate.getId());
            recipeVO.setRateValue(rate.getScore());
        }
        //判断"我"是否关注发布该菜谱的作者
        recipeVO.setDoIFollowWriter(followService.judgeDoIFollowUser(myId, recipeVO.getUserId()));

        //获取总的点赞数
        recipeVO.setLikedCounts(likeService.getBeLikedCounts(LikeTypeEnum.RECIPE.getCode(), recipeId));
        //获取总的评论数
        recipeVO.setCommentCounts(commentService.getCommentCounts(CommentTypeEnum.RECIPE.getCode(), recipeId));

        return GraceJSONResult.ok(recipeVO);
    }

    @ApiOperation(value = "分页根据用户ID获取其喜欢的菜谱列表")
    @PostMapping("/getLikeRecipeByUserId")
    public GraceJSONResult getLikeRecipeByUserId(@RequestBody Page page, @RequestParam("userId") int userId){
        log.info("page----size:{}, total:{}, current:{}",page.getSize(),page.getTotal(),page.getCurrent());
        IPage<RecipeShowVO> contentIPage = recipeService.getLikeRecipeByUserId(page, userId);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "分页根据用户ID获取其发布的菜谱列表")
    @PostMapping("/getRecipeListByUserId")
    public GraceJSONResult getRecipeListByUserId(@RequestBody Page page, @RequestParam("userId") int userId, @RequestParam("isDifferOnShow") Boolean isDifferOnShow){
        int state = RecipeStateEnum.NOT_DIFFER.getState();
        if(isDifferOnShow){
            state = RecipeStateEnum.ON_SHOW.getState();
        }
        IPage<RecipeShowVO> contentIPage = recipeService.getRecipeListByUserId(page, userId, state);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }

    @ApiOperation(value = "新增整个菜谱信息")
    @PostMapping("/addRecipeWhole")
    public GraceJSONResult addRecipeWhole(@RequestBody RecipeBO recipeBO){
        Recipe recipeResult = recipeService.insertRecipe(recipeBO);
        if(recipeResult == null){
            GraceJSONResult.errorMsg("菜谱基本信息添加失败！");
        }
        List<Step> steps = recipeBO.getSteps();
        for (Step step : steps) {
            step.setRecipeId(recipeResult.getId());
        }
        if(stepService.insertOrUpdateStepList(steps)==0 && steps.size()!=0){
            return GraceJSONResult.errorMsg("步骤信息添加失败！");
        }
        List<Material> materials = recipeBO.getMaterials();
        for (Material material : materials) {
            material.setRecipeId(recipeResult.getId());
        }

        if(materialService.insertOrUpdateMaterialList(materials)==0 && materials.size()!=0){
            return GraceJSONResult.errorMsg("食材用料信息添加失败！");
        }
        return GraceJSONResult.ok("菜谱全部信息添加成功！");
    }

    @ApiOperation(value = "更新整个菜谱信息")
    @PostMapping("/updateRecipeWhole")
    public GraceJSONResult updateRecipeWhole(@RequestBody RecipeToUpdateBO recipeToUpdateBO){
        //返回1表示插入成功；-1表示插入后的已存在，拒绝插入；0表示插入数据库操作失败
        int count;
        Recipe recipe = new Recipe();
        BeanUtils.copyProperties(recipeToUpdateBO, recipe);
        count = recipeService.updateRecipeById(recipe);//修改基本信息
        log.info("菜谱基本信息修改影响条数："+count);
        List<Step> steps = recipeToUpdateBO.getSteps();
        for (Step step : steps) {
            step.setRecipeId(recipe.getId());
        }
        count = stepService.insertOrUpdateStepList(steps);//更新步骤信息：修改+新增未有的
        log.info("批量更新步骤信息影响条数："+count);
        List<Integer> stepIdsToDel = recipeToUpdateBO.getStepIdsToDel();
        if(stepIdsToDel != null && stepIdsToDel.size()!= 0){
            count = stepService.delStepByIds(stepIdsToDel);
            log.info("批量删除步骤信息影响条数："+count);
        }

        List<Material> materials = recipeToUpdateBO.getMaterials();
        for (Material material : materials) {
            material.setRecipeId(recipe.getId());
        }
        count = materialService.insertOrUpdateMaterialList(materials);//更新食材用料信息：修改+新增未有的
        log.info("批量更新食材信息影响条数："+count);

        List<Integer> materialIdsToDel = recipeToUpdateBO.getMaterialIdsToDel();
        if(materialIdsToDel != null && materialIdsToDel.size()!=0){
            count = materialService.delMaterialByIds(materialIdsToDel);
            log.info("批量删除步骤信息影响条数："+count);
        }
        return GraceJSONResult.ok("菜谱全部信息更新成功！");
    }
    @ApiOperation(value = "上架、下架")
    @PostMapping("/updateRecipeStateById")
    public GraceJSONResult updateRecipeStateById(@RequestParam("recipeId") int recipeId, @RequestParam("state") int state){
        if(recipeService.updateStateById(recipeId,state) == 1){
            return GraceJSONResult.ok();
        }else{
            return GraceJSONResult.notifyMsg("后台操作失败");
        }

    }

    @ApiOperation(value = "根据id删除菜谱信息")
    @PostMapping("/deleteRecipeWhole")
    public GraceJSONResult deleteRecipeWhole(@RequestParam("recipeId") int recipeId){
        int count = recipeService.deleteById(recipeId);
        log.info("删除基本信息条数："+ count );
        stepService.deleteByRecipeId(recipeId); //数据路库设置了级联删除
        materialService.deleteByRecipeId(recipeId); //数据路库设置了级联删除
        return GraceJSONResult.ok();
    }

    @ApiOperation(value = "首页-新谱上线")
    @PostMapping("/getLatestRecipes")
    public GraceJSONResult getLatestRecipes(@RequestBody Page page){
        log.info("page----size:{}, total:{}, current:{}",page.getSize(),page.getTotal(),page.getCurrent());
        IPage<RecipeShowVO> contentIPage = recipeService.getLatestRecipeList(page);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }
    @ApiOperation(value = "首页-每周热榜")
    @PostMapping("/getWeeklyHotRecipes")
    public GraceJSONResult getWeeklyHotRecipes(@RequestBody Page page){
        log.info("page----size:{}, total:{}, current:{}",page.getSize(),page.getTotal(),page.getCurrent());
        IPage<RecipeShowVO> contentIPage = recipeService.getWeeklyHotRecipeList(page);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }
    @ApiOperation(value = "首页-新手入门")
    @PostMapping("/getEasyRecipes")
    public GraceJSONResult getEasyRecipes(@RequestBody Page page){
        log.info("page----size:{}, total:{}, current:{}",page.getSize(),page.getTotal(),page.getCurrent());
        IPage<RecipeShowVO> contentIPage = recipeService.getEasyRecipeList(page);
        return DuplicateMethod.getDataListByPage(contentIPage,page);
    }
}

