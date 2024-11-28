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
public class SecondSort implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 菜谱二级类别id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜谱二级类别名称
     */
    private String typeName;

    /**
     * 菜谱二级类别图片
     */
    private String image;

    /**
     * 菜谱一级类别id
     */
    private Integer parent;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
      @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
