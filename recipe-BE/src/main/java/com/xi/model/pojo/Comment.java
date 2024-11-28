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
@TableName("`comment`")
public class Comment implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 评论表id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者id
     */
    private Integer fromUid;

    /**
     * 主题id
     */
    private Integer topicId;

    /**
     * 复用评论模块1：对菜谱评论；2：对用户动态评论
     */
    private Integer topicType;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
