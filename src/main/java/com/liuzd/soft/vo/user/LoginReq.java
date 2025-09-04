package com.liuzd.soft.vo.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
public class LoginReq {
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "用户名不能为空")
    private String username;
}
