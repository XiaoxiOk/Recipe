package com.xi.model.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
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
@TableName("`like`")
public class Like implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 点赞id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 对应的菜谱/作品/评论/回复的id
     */
    private Integer typeId;

    /**
     * 点赞类型：1菜谱点赞收藏；2作品点赞；3评论/回复点赞
     */
    private Integer type;

    /**
     * 用户id
     */
    private Integer userId;


    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
