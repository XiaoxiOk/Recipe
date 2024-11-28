package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xi.mapper.MaterialMapper;
import com.xi.model.pojo.Material;
import com.xi.service.MaterialService;
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
public class MaterialServiceImpl implements MaterialService {
    @Resource
    private MaterialMapper materialMapper;
    

    //根据菜谱ID获取其下所有步骤信息
    public List<Material> getMaterialsByRecipeId(int recipeId){
        return materialMapper.getMaterialsByRecipeId(recipeId);
    }


    //批量修改某个菜谱的步骤信息，有则修改，无则插入/新增
    public int insertOrUpdateMaterialList(List<Material> materialList){
        return materialMapper.insertOrUpdateMaterialList(materialList);
    }


    //批量删除步骤信息
    public int delMaterialByIds(List<Integer> ids){
        return materialMapper.delMaterialByIds(ids);
    }


    //根据菜谱id删除所有步骤
    public int deleteByRecipeId(int recipeId){
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recipe_id", recipeId);
        return  materialMapper.delete(queryWrapper);
    }
}
