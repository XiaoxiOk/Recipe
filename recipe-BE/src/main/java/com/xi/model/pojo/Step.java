package com.xi.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Step implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 步骤表id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜谱id
     */
    private Integer recipeId;

    /**
     *  步骤序号
     */
    private Integer seqNumber;

    /**
     * 步骤描述
     */
    private String description;

    /**
     * 步骤示意图片/视频
     */
    private String imgUrl;


}
