package com.xi.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplyVO {
    private Integer id; //回复ID
    private String content; //回复内容
    private Integer replyId; //回复目标评论/回复id
    private Integer commentId; //comment_id==reply_id时表示回复顶层评论，不等时是对回复的回复
    private Integer fromUid; //回复人id
    private Integer toUid; //目标人id
    private Date createTime; //回复时间

    private String fromUserName; //评论者昵称
    private String toUserName;   //回复目标者昵称
    private String profilePhoto; //评论者头像

    private Integer likedCounts = 0; //被点赞总计数量
    private boolean doILikeThis = false; //标识当前用户是否点赞过该回复
}
