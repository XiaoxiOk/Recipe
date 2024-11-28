package com.xi.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 前端传给后端的用户信息 用于修改更新
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserBO {
    private Integer id;
    private String userName;
    private Integer gender;
    private Date birthday;
    private String mobile;
    private String profilePhoto;
}
