package com.xi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xi.model.bo.RecipeBO;
import com.xi.model.dto.RelateDTO;
import com.xi.model.po.RateToSTATS;
import com.xi.model.pojo.Rate;
import com.xi.model.pojo.Recipe;
import com.xi.model.vo.RecipeShowVO;
import com.xi.model.vo.RecipeVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wml
 * @since 2023-05-14
 */
public interface RateService {
    List<RelateDTO> getRateDTOData();
    Rate findRate(int userId, int recipeId);
    Rate addRate(Rate rate);
    int updateRate(int rateId, int score);
    int deleteRate(int rateId);
    RateToSTATS getRateToSTATSByUserId(Integer userId);
    Integer findScoreByUserAndRecipeId(int userId, int recipeId);
    List<Integer> findAllUserIds();
    List<Integer> findAllRecipeIds();
    List<Integer> findCommonRecipeIdsByUsers(Integer userId1, Integer userId2);

    double getAverageScoreByUser(Integer userId);
    Map<String, Double> getAVGAndSTDByUserId(Integer userId);

    boolean isNotRateRecipe(int userId);
}
