package com.xi.service.impl;

import com.xi.service.RateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RateServiceTest {
    @Resource
    private RateService rateService;

    @Test
    public void test_findCommonRecipeIdsByUsers(){
        Integer userId1 = 3;
        Integer userId2 = 4;
        List<Integer> recipeIds = rateService.findCommonRecipeIdsByUsers(userId1,userId2);
        System.out.println("get the List<RecipeId>:");
        for (Integer recipeId: recipeIds
             ) {
            System.out.print(recipeId+";");
        }
    }
    @Test
    public void getRateToSTATSByUserId() {
        System.out.println(rateService.getRateToSTATSByUserId(3));
    }
}
