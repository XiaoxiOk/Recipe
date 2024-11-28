package com.xi.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
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
public class Material implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用料表id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜谱id
     */
    private Integer recipeId;

    /**
     * 食材名称
     */
    private String foodName;

    /**
     * 食材用量（克；只；勺...）
     */
    private String consumption;


}
