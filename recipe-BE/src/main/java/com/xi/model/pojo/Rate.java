package com.xi.model.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Rate implements Serializable {
    private static final long serialVersionUID=1L;

    /**
     * 消息通知id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 评分值1-5
     */
    private Integer score;


    /**
     * 菜谱编号
     */
    private Integer recipeId;

    /**
     * 来自用户id
     */
    private Integer userId;

    /**
     * 菜谱更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
