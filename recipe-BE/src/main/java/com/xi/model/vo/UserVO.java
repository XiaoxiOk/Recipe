package com.xi.model.vo;

import com.xi.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVO {
    private int id;
    private String mobile;
    private String userName;
    private int gender;
    private Date birthday;
    private Integer frozen;
    private String profilePhoto;
    private Date createTime;
    private Date updateTime;

    private Object tokenInfo;
    private String userToken;   // 用户token，传递给前端

    private Integer myFollowsCounts;
    private Integer myFansCounts;
}
