package com.xi.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FollowUserVO {
    private int userId;
    private String userName;
    private String profilePhoto;
    private boolean mutual = false;
}
