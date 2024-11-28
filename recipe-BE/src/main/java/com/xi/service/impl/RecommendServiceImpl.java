package com.xi.service.impl;

import com.xi.common.core.CoreMath;
import com.xi.model.dto.RelateDTO;
import com.xi.service.LikeService;
import com.xi.service.RateService;
import com.xi.service.RecipeService;
import com.xi.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 假设有一个User类，包含id和name属性
// 假设有一个Recipe类，包含id和name属性
// 假设有一个Rating类，包含userId, foodId和score属性，表示用户对菜谱的评分
// 假设有一个RatingRepository接口，用于从数据库中查询评分数据
// 假设有一个RecipeRepository接口，用于从数据库中查询菜谱数据

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private LikeService likeService;
    @Autowired
    private RateService rateService;

    @Autowired
    private RecipeService recipeService;
    /**
     * 方法描述: 猜你喜欢
     *
     * @param userId 用户id
     * @Return {@link List<Integer>}
     */
    public List<Integer> userCfRecommend(int userId, int maxSize){
        List<RelateDTO> data= rateService.getRateDTOData();
        List<Integer> recommendations = recommend(userId, data, maxSize);
        System.out.println("recommendations"+recommendations.toString());
        List<Integer> recipeIds = recipeService.getIdList();
        return recipeIds.stream().filter(integer->recommendations.contains(integer)).collect(Collectors.toList());
    }


    /**
     * 方法描述: 推荐菜谱id列表
     *
     * @param userId 当前用户
     * @param list 用户菜谱评分数据
     * @return {@link List <Integer>}
     */
    public List<Integer> recommend(Integer userId, List<RelateDTO> list, int maxSize) {
        //按用户分组
        Map<Integer, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));
        //获取其他用户与当前用户的关系值
        Map<Integer, Double> userDisMap = CoreMath.computeNeighbor(userId, userMap, 0);

        System.out.println("userMap:"+ userDisMap.toString());
        Set<Integer> userIds = new HashSet<>();
        //按相似度从高到低排序获取关系最近用户,最多maxSize个
        userDisMap.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    System.out.println("相似用户（高->低）:"+entry.getKey());
                    // 将前maxSize个用户加入相似列表
                    if (userIds.size() < maxSize) {
                        userIds.add(entry.getKey());
                    }
                });
        System.out.println("maxSize="+maxSize+"时，相似用户：" + userIds.toString());
        if (userIds.size() == 0) {
            return Collections.emptyList();
        }
        // 最近邻用户喜欢的菜谱列表
        List<Integer> neighborItems = new ArrayList<>();
        userIds.forEach(nearUId -> {
            List<Integer> recipeIds = likeService.getRecipesByUserId(nearUId);
            if(recipeIds != null){
                neighborItems.addAll(recipeIds);
            }

        });
        //指定用户喜欢的菜谱列表
        List<Integer> userItems = likeService.getRecipesByUserId(userId);

        //找到近邻喜欢的，但是该用户没喜欢过的菜谱
        neighborItems.removeAll(userItems);
        return neighborItems;

    }
}