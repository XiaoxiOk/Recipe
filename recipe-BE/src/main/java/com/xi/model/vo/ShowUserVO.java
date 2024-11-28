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
public class ShowUserVO {
    private int id;
    private String mobile;
    private String userName;
    private int gender;
    private Date birthday;
    private Integer frozen;
    private String profilePhoto;
    private Date createTime;

    private Integer thisFollowsCounts;
    private Integer thisFansCounts;
    private Boolean doIFollowThisUser = false;
}
