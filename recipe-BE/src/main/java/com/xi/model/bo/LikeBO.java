package com.xi.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 前端传给后端的点赞信息
 * type:1菜谱、2用户动态、3评论、4回复
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LikeBO {
    private Integer id;
    private Integer type;
    private Integer typeId;
    private Integer userId;
}
