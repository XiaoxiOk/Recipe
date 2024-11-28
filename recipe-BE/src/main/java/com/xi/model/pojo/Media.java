package com.xi.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableId;

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
public class Media implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 图片id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片封面url
     */
    private String coverUrl;

    /**
     * 实际文件url，包括图片、视频
     */
    private String fileUrl;


    /**
     * 用户动态id
     */
    private Integer userShareId;



}
