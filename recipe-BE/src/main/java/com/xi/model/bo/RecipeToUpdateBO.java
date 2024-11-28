package com.xi.model.bo;

import com.xi.model.pojo.Material;
import com.xi.model.pojo.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecipeToUpdateBO {
    private Integer id;

    /**
     * 菜谱名简介
     */
    private String recipeName;

    /**
     * 菜谱描述信息
     */
    private String detail;

    /**
     * 发布者id
     */
    private Integer userId;

    /**
     * 被点赞收藏数量
     */
    private Integer star;

    /**
     * 0草稿箱；1待审核，2表示审核通过上架，-1表示审核不通过，-2表示下架
     */
    private Integer state;

    /**
     * 烹饪难度：0:零基础；1：容易；2：较难；3：相当难
     */
    private Integer difficulty;

    /**
     * 封面图
     */
    private String showImage;

    /**
     * 媒体路径，图像/视频
     */
    private String mediaUrl;

    /**
     * 二级类别id
     */
    private Integer secSortId;


    private List<Step> steps;

    private List<Material> materials;

    private List<Integer> materialIdsToDel;

    private List<Integer> stepIdsToDel;
}
