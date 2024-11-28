package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xi.mapper.StepMapper;
import com.xi.model.pojo.Step;
import com.xi.service.StepService;
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
public class StepServiceImpl implements StepService {


    @Resource
    private StepMapper stepMapper;

    //根据菜谱ID获取其下所有步骤信息
    public List<Step> getStepsByRecipeId(int recipeId){
        return stepMapper.getStepsByRecipeId(recipeId);
    }


    //批量修改某个菜谱的步骤信息，有则修改，无则插入/新增
    public int insertOrUpdateStepList(List<Step> stepList){
        return stepMapper.insertOrUpdateStepList(stepList);
    }

    //批量删除步骤信息
    public int delStepByIds(List<Integer> ids){
        return stepMapper.deleteStepByIds(ids);
    }
    public int updateStepById(Step step){
        return stepMapper.updateById(step);
    }
    

    public int deleteById(int id){
        return stepMapper.deleteById(id);
    }

    //根据菜谱id删除所有步骤
    public int deleteByRecipeId(int recipeId){
        QueryWrapper<Step> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recipe_id", recipeId);
        return stepMapper.delete(queryWrapper);
    }
}
