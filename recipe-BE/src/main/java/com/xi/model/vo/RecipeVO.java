package com.xi.model.vo;

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
public class RecipeVO {
    private Integer id;
    private String recipeName; //菜谱名简介
    private String detail; //菜谱描述信息
    private Integer userId; //发布者id
    private Integer star; //被点赞收藏数量
    private Integer state; //0草稿箱；1待审核，2表示审核通过上架，-1表示审核不通过，-2表示下架
    private Integer difficulty; //烹饪难度：0:零基础；1：容易；2：较难；3：相当难
    private String showImage; //封面图
    private String mediaUrl; //媒体文件路径
    private Integer secSortId; //二级类别id

    private String userName; //用户名
    private String profilePhoto; //用户头像
    private String typeName; //二级类别名称“标签”

    private List<Step> steps;
    private List<Material> materials;

    private Integer rateId = -1; //评分ID
    private Integer rateValue = 0; //评分值

    private boolean doIFollowWriter = false;
    private boolean doILikeThis = false;

    private Integer likedCounts;//被点赞总计数量
    private Integer commentCounts; //被评论总计数量
}
