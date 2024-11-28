package com.xi.model.vo;

import com.xi.model.pojo.Material;
import com.xi.model.pojo.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecipeShowVO {
    private Integer id;
    private String recipeName; //菜谱名简介
    private String detail; //菜谱描述信息
    private Integer difficulty; //烹饪难度：0:零基础；1：容易；2：较难；3：相当难
    private Integer state; //状态：0正常,1已被下架
    private String showImage; //封面图
    private String mediaUrl; //媒体文件路径
    private Date createTime; // 动态发布时间

    private String userName; //用户名
    private String profilePhoto; //用户头像
}
