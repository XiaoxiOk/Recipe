package com.xi.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class Follow implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 关注id
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 来自用户id
     */
    private Integer fromUid;

    /**
     * 目标用户id
     */
    private Integer toUid;

    /**
     * 1互关；0仅一方
     * 新增、一方取关都要及时更新；
     * 目的是给前端展示时减少查询次数
     */
    private Integer mutual;


}
