package com.xi.mapper;

import com.xi.model.dto.RelateDTO;
import com.xi.model.po.RatePO;
import com.xi.model.pojo.Rate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Repository
public interface RateMapper extends BaseMapper<Rate>{

    @Select("select user_id userId, recipe_id recipeId, score from rate")
    List<RelateDTO> selectRateDTOData();

    @Select("select recipe_id recipeId from rate where user_id = #{userId}")
    List<Integer> selectRecipeIdsByUsers(@Param("userId") Integer userId);
    @Select("SELECT IFNULL(AVG(score),0) avgScore FROM rate WHERE user_id = #{userId}")
    Double getAverageScoreByUser(@Param("userId") Integer userId);

    @Select("SELECT FORMAT(IFNULL(AVG(score),0),4) avgScore, FORMAT(IFNULL(STD(score), 0),4) FROM rate WHERE user_id =#{userId}")
    Map<String, Double> getAVGAndSTDByUserId(@Param("userId") Integer userId);

    @Select("SELECT recipe_id recipeId, score FROM rate WHERE user_id = #{userId}")
    List<RatePO> getRateByUserId(@Param("userId") Integer userId);

    @Select("SELECT score FROM rate WHERE user_id = #{userId} AND recipe_id = #{recipeId}")
    Integer getScoreByUserIdAndRecipeId(@Param("userId") Integer userId,@Param("recipeId") Integer recipeId);

    @Select("SELECT DISTINCT user_id userId FROM rate")
    List<Integer> getAllUserIds();
    @Select("SELECT DISTINCT recipe_id recipeId FROM rate")
    List<Integer> getAllRecipeIds();
}
