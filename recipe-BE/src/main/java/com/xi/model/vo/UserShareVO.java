package com.xi.model.vo;

import com.xi.model.pojo.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserShareVO {

    private Integer id; //用户动态id
    private Integer uid; //用户id
    private String content; //用户发表内容（256bytes）
    private Date createTime; // 动态发布时间
    private List<Media> mediaList; //动态所含所有图片

    private String userName; //用户名
    private String profilePhoto; //用户头像


    private boolean doIFollowWriter = false; //是否关注
    private boolean doILikeThis = false;    //是否已点赞

    private Integer likedCounts;//被点赞总计数量
    private Integer commentCounts;//被评论总计数
}
