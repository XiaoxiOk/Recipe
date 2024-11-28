package com.xi.model.bo;

import com.xi.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 前端传给后端的对象
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegistLoginBO {
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    private String userName;
    private int gender = 0;//默认为0未选择,1男，2女
    private Date birthday = DateUtil.stringToDate("1900-01-01");
    private String profilePhoto;
}
