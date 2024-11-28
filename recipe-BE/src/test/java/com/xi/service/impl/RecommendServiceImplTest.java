package com.xi.service.impl;

import com.xi.service.RecommendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendServiceImplTest {
    @Resource
    private RecommendService recommendService;

//    @Test
//    public  void test(){
//        Integer userId = 3;
//        List<Integer> recommendations = recommendService.recommend(userId);
//        System.out.println("为userId=【"+userId+"】的用户个性化推荐菜谱ID："+recommendations.toString());
//    }

    @Test
    public  void test_UserCF(){
        Integer userId = 3;
        List<Integer> recommendations = recommendService.userCfRecommend(userId,3);
        System.out.println("为userId=【"+userId+"】的用户个性化推荐菜谱ID："+recommendations.toString());
    }

}
