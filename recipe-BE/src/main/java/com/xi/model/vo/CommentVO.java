package com.xi.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {
    private int id;
    private String content;
    private int fromUid;
    private int topicId;
    private int topicType;
    private Date createTime;

    private String userName; //评论者昵称
    private String profilePhoto; //评论者头像

    private int likedCounts = 0; //被点赞总计数量
    private int repliedCounts = 0; //被回复总计数量
    private boolean doILikeThis = false; //标识当前用户是否点赞过该评论
}
