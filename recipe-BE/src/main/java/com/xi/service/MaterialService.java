package com.xi.service;

import com.xi.model.pojo.Material;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface MaterialService{
    public List<Material> getMaterialsByRecipeId(int recipeId);
    public int insertOrUpdateMaterialList(List<Material> materialList);
    public int delMaterialByIds(List<Integer> ids);
    public int deleteByRecipeId(int recipeId);
}
