package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.common.enums.RecipeStateEnum;
import com.xi.controller.RecipeController;
import com.xi.mapper.RecipeMapper;

import com.xi.model.bo.RecipeBO;
import com.xi.model.pojo.Recipe;
import com.xi.model.vo.RecipeShowVO;
import com.xi.model.vo.RecipeVO;
import com.xi.service.RecipeService;
import org.springframework.beans.BeanUtils;
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
public class RecipeServiceImpl implements RecipeService {

    @Resource
    private RecipeMapper recipeMapper;


    //分页获取菜谱基本信息
    public IPage<Recipe> getRecipeBasicByPage(Page<Recipe> page, int state){
        return recipeMapper.selectListByPage(page,state);
    }

    @Override
    public IPage<Recipe> getRecipeFilterByPage(Page<Recipe> page, int state, List<Integer> recommendRecipeIds) {
        return recipeMapper.selectRecipeFilterByPage(page, state, recommendRecipeIds);
    }

    public IPage<Recipe> selectRecipeByNameOrState(Page<Recipe> page, String inputStr, int state){

        return recipeMapper.searchRecipeByNameOrState(page, inputStr, state);
    }

    @Override
    public IPage<RecipeShowVO> showRecipeByNameOrSecId(Page<RecipeShowVO> page, String inputStr, int secId) {
        return recipeMapper.showRecipeByNameOrSecId(page, inputStr, secId, 0);//state=0上架状态
    }

    public IPage<RecipeShowVO> showRecipeByNameOrState(Page<RecipeShowVO> page, String inputStr, int state){
{
    return recipeMapper.showRecipeByNameOrState(page, inputStr, state);
}

    }

    public IPage<RecipeVO> getRecipeVOByPage(Page<RecipeVO> page, int state){
        return recipeMapper.selectRecipeVOByPage(page,state);
    }

    public IPage<RecipeShowVO> getLikeRecipeByUserId(Page page, int userId){
        return recipeMapper.getLikeRecipeByUserId(page, userId);
    }
    public IPage<RecipeShowVO> getRecipeListByUserId(Page<RecipeShowVO> page, int userId, int state){
        return recipeMapper.getRecipeListByUserId(page, userId, state);
    }

    public IPage<RecipeShowVO> getLatestRecipeList(Page<RecipeShowVO> page){
        return recipeMapper.getLatestRecipeList(page);
    }
    public IPage<RecipeShowVO> getWeeklyHotRecipeList(Page<RecipeShowVO> page){
        return recipeMapper.getWeeklyHotRecipeList(page);
    }
    public IPage<RecipeShowVO> getEasyRecipeList(Page<RecipeShowVO> page){
        return recipeMapper.getEasyRecipeList(page);
    }
    public Recipe getRecipeBasicById(int recipeId){
        return recipeMapper.selectById(recipeId);
    }

    public RecipeVO getRecipeUserById(int recipeId){
        return recipeMapper.queryRecipeUserById(recipeId);
    }
    public Recipe insertRecipe(RecipeBO recipeBO){
        Recipe recipe = new Recipe();
        BeanUtils.copyProperties(recipeBO, recipe);
        recipe.setState(RecipeStateEnum.ON_SHOW.getState());
        int count = recipeMapper.insert(recipe);
        if(count == 1){
            return recipe; //插入成功后用户id会自动填充!
        }else{
            return null;
        }
    }

    public int updateRecipeById(Recipe recipe){
        return recipeMapper.updateById(recipe);
    }

    public long updateStateById(int id, int state){
        Recipe recipe = new Recipe();
        recipe.setState(state);
        UpdateWrapper<Recipe> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        return recipeMapper.update(recipe, updateWrapper);
    }

    public int deleteById(int id){
        return recipeMapper.deleteById(id);
    }

    @Override
    public List<Integer> getIdList() {
        return recipeMapper.selectIds();
    }

    /**
     * 根据菜谱ID列表获取基本信息,过滤到被下架的菜谱
     * @param idList 菜谱ID列表
     * @return 菜谱基本信息列表
     */
    @Override
    public List<Recipe> selectByIdList(List<Integer> idList) {
        return recipeMapper.selectByIdList(idList);
    }

}
