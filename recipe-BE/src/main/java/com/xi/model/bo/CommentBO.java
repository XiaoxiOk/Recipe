package com.xi.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 前端传给后端的评论信息 用于删除评论
 * topicType:1菜谱、2用户动态
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentBO {
    private int id;
    private int topicId;
    private int topicType;
}
