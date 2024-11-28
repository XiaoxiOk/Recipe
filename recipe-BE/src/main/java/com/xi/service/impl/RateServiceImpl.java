package com.xi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xi.mapper.RateMapper;
import com.xi.model.dto.RelateDTO;
import com.xi.model.po.RatePO;
import com.xi.model.po.RateToSTATS;
import com.xi.model.pojo.Rate;
import com.xi.service.RateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RateServiceImpl implements RateService {
    @Resource
    private RateMapper rateMapper;

    @Override
    public List<RelateDTO> getRateDTOData() {
        return rateMapper.selectRateDTOData();
    }

    /**
     * 查找用户对菜谱是否进行了评分
     * @param userId 用户ID
     * @param recipeId 菜谱ID
     * @return 评分对象
     */
    @Override
    public Rate findRate(int userId, int recipeId){
        QueryWrapper<Rate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("recipe_id", recipeId);

        return rateMapper.selectOne(queryWrapper);
    }

    /**
     * 新增一个评分对象
     * @param rate 评分对象
     * @return 该评分对象
     */
    @Override
    public Rate addRate(Rate rate){
        int count = rateMapper.insert(rate);
        if(count == 1){
            return rate; //插入成功后用户id会自动填充!
        }else{
            return null;
        }
    }

    /**
     * 根据评分ID更新一个评分对象
     * @param rateId 评分对象ID
     * @param score 新的评分数
     * @return 影响条数
     */
    @Override
    public int updateRate(int rateId, int score){
        UpdateWrapper<Rate> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",rateId);
        updateWrapper.set("score", score);
        return rateMapper.update(null, updateWrapper);
    }

    @Override
    public int deleteRate(int rateId){
        return rateMapper.deleteById(rateId);
    }

    /**
     * 跟据用户ID获取其评价的菜谱以及评分
     * @param userId 用户ID
     * @return {userId, recipeIds, scores}
     */
    @Override
    public RateToSTATS getRateToSTATSByUserId(Integer userId){
        List<RatePO> ratePOS = rateMapper.getRateByUserId(userId);
        RateToSTATS rateToSTATS = new RateToSTATS();
        rateToSTATS.setUserId(userId);
        rateToSTATS.setRecipeIds(ratePOS.stream().map(RatePO::getRecipeId).collect(Collectors.toList()));
        rateToSTATS.setScores(ratePOS.stream().map(RatePO::getScore).collect(Collectors.toList()));
        return rateToSTATS;
    }

    /**
     * 查看用户对某菜谱的评分
     * @param userId 用户ID
     * @param recipeId 菜谱ID
     * @return 评分值
     */
    @Override
    public Integer findScoreByUserAndRecipeId(int userId, int recipeId) {
        return rateMapper.getScoreByUserIdAndRecipeId(userId, recipeId);
    }
    /**
     * 获取评分表中所有用户ID
     * @return 用户IDList
     */
    @Override
    public List<Integer> findAllUserIds() {
        return rateMapper.getAllUserIds();
    }

    @Override
    public List<Integer> findAllRecipeIds() {
        return rateMapper.getAllRecipeIds();
    }

    @Override
    public List<Integer> findCommonRecipeIdsByUsers(Integer userId1, Integer userId2) {
        List<Integer> recipeIdsFromUid1 = rateMapper.selectRecipeIdsByUsers(userId1);
        List<Integer> recipeIdsFromUid2 = rateMapper.selectRecipeIdsByUsers(userId2);
        //获取两个集合的交集
        List<Integer> intersectionList
                = recipeIdsFromUid1.stream().filter(recipeIdsFromUid2::contains).collect(Collectors.toList());

        System.out.println(intersectionList);

        return intersectionList;
    }



    /**
     * 获取用户评分的平均值
     * @param userId
     * @return
     */
    @Override
    public double getAverageScoreByUser(Integer userId) {

        return rateMapper.getAverageScoreByUser(userId);
    }

    /**
     * 获取平均值和标准差
     * @param userId
     * @return
     */
    @Override
    public Map<String, Double> getAVGAndSTDByUserId(Integer userId) {

        return rateMapper.getAVGAndSTDByUserId(userId);
    }

    /**
     * 判断用户是否评分过任一菜谱
     * @param userId
     * @return
     */
    @Override
    public boolean isNotRateRecipe(int userId) {
        QueryWrapper<Rate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        long count = rateMapper.selectCount(queryWrapper);
        if(count == 0){
            return true;
        }
        return false;
    }

}
