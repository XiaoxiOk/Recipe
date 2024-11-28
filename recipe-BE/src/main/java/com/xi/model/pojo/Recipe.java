package com.xi.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author wml
 * @since 2023-02-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class Recipe implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
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
     * 0正常可查看, 1下架
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

    /**
     * 菜谱创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 菜谱更新时间
     */
      @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
