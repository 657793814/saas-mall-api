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
public class LoginResp<T> {
    private String token;
    private String randStr;
    private long timestamp;
    private T info;
}
