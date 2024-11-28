package com.xi.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SortVO {
    private int id;   //一级类别id
    private String typeName;//一级类别名称
    private List<SecSortBasicVO> children;
}
