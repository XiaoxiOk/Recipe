package com.xi.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用于获取数据库数据进行处理
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RatePO {
    private Integer recipeId;
    private Integer score;
}
