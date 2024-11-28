package com.xi.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 前端传给后端的回复信息 用于删除回复
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReplyBO {
    private int id; //回复ID
    private int commentId;//回复评论ID
}
