package com.liuzd.soft.vo.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
public class RegisterResp {
    private String uuid;
    private String username;
    private String mobile;
}
