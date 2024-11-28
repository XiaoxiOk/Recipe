package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.bo.RecipeBO;
import com.xi.model.pojo.Recipe;
import com.xi.model.vo.RecipeShowVO;
import com.xi.model.vo.RecipeVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface RecipeService {
    IPage<Recipe> getRecipeBasicByPage(Page<Recipe> page, int state);
    IPage<Recipe> getRecipeFilterByPage(Page<Recipe> page, int state, List<Integer> recommendRecipeIds);
    IPage<Recipe> selectRecipeByNameOrState(Page<Recipe> page, String inputStr, int state);
    IPage<RecipeShowVO> showRecipeByNameOrSecId(Page<RecipeShowVO> page, String inputStr, int secId);
    IPage<RecipeShowVO> showRecipeByNameOrState(Page<RecipeShowVO> page, String inputStr, int state);
    IPage<RecipeVO> getRecipeVOByPage(Page<RecipeVO> page, int state);
    IPage<RecipeShowVO> getLikeRecipeByUserId(Page page, int userId);
    IPage<RecipeShowVO> getRecipeListByUserId(Page<RecipeShowVO> page, int userId, int state);
    Recipe getRecipeBasicById(int recipeId);
    RecipeVO getRecipeUserById(int recipeId);
    Recipe insertRecipe(RecipeBO recipeBO);
    int updateRecipeById(Recipe recipe);
    long updateStateById(int id, int state);
    int deleteById(int id);
    List<Integer> getIdList();
    List<Recipe> selectByIdList(List<Integer> idList);
    IPage<RecipeShowVO> getLatestRecipeList(Page<RecipeShowVO> page);
    IPage<RecipeShowVO> getWeeklyHotRecipeList(Page<RecipeShowVO> page);
    IPage<RecipeShowVO> getEasyRecipeList(Page<RecipeShowVO> page);
}
