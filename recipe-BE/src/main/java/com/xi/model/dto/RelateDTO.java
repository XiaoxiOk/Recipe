package com.xi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelateDTO {
    /** 用户id */
    private Integer userId;
    /** 菜谱id */
    private Integer recipeId;
    /** 分数 */
    private Integer score;
}
