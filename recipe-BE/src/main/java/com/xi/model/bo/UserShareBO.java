package com.xi.model.bo;

import com.xi.model.pojo.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserShareBO {
    /**
     * 用户动态id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户发表内容（256bytes）
     */
    private String content;

    /**
     * 动态图片
     */
    private List<Media> mediaList;
}
