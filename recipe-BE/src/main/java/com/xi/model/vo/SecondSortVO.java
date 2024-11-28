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
public class SecondSortVO {
    private int secId;//二级类别id
    private String secName;//二级类别更新时间
    private String image;//二级类别展示图片
    private int firstId;   //一级类别id
    private String firstName;//一级类别名称
    private Date createTime;//二级类别创建时间
    private Date updateTime; //二级类别更新时间
}
