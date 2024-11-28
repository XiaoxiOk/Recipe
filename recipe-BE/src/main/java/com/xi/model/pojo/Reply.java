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
public class Reply implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 回复id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 回复目标评论/评论id
     */
    private Integer replyId;


    /**
     * comment_id==reply_id时表示回复顶层评论，不等时是对回复的回复
     */
    private Integer commentId;

    /**
     * 回复人id
     */
    private Integer fromUid;

    /**
     * 目标人id
     */
    private Integer toUid;

    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
