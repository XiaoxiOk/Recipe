package com.xi.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 用于获取数据库数据进行处理
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RateToSTATS {
    Integer userId;
    List<Integer> recipeIds;
    List<Integer> scores;
}
