package com.xi.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageVO {
    private Integer id; //消息ID
    private Map<String, Object> content; //消息内容 用于配合消息展示的数据:视类型而定
    private Integer type; //消息通知类型见MessageTypeEnum
    private Integer typeId; //通知目标ID
    private Integer fromUid; //来自用户id
    private Integer toUid; //发送目标用户id
    private Integer isRead; //该消息是否被toUser阅读过：1是，0否
    private Date createTime; //消息创建时间

    private String fromUserName; //来自用户昵称
    private String fromUserPhoto; //来自用户头像

}
