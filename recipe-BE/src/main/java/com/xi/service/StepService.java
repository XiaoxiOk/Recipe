package com.xi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xi.model.pojo.Step;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
public interface StepService {
    public List<Step> getStepsByRecipeId(int recipeId);
    public int insertOrUpdateStepList(List<Step> stepList);
    public int delStepByIds(List<Integer> ids);
    public int deleteByRecipeId(int recipeId);
}
