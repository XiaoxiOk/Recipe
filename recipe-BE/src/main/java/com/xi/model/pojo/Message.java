package com.xi.model.pojo;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import lombok.experimental.Accessors;

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
public class Message implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 消息通知id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息通知类型：1：点赞；2：评论；3：回复；41：系统通知菜谱审核成功可以上架；42：系统通知菜谱下架；43：系统通知菜谱不通过；5：其他通知
     */
    private Integer type;

    /**
     * 通知目标：1：菜谱id；2：动态id；3回复id；0其他
     */
    private Integer typeId;

    /**
     * 来自用户id
     */
    private Integer fromUid;

    /**
     * 发送目标用户id
     */
    private Integer toUid;

    /**
     * 该消息是否被toUser阅读过：1是，0否
     */
    private Integer isRead;
    /**
     * 创建时间
     */
      @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
