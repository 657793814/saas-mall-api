package com.liuzd.soft.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RegisterReq {
    @NotBlank
    private String username;
    @NotBlank
    private String mobile;
    @NotBlank
    private String password;
    @NotBlank
    @JsonProperty("verificationCode")
    private String captcha;
}
