package com.liuzd.soft.dto.token;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: liuzd
 * @date: 2025/8/19
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer uid;
    private String uuid;
    private String userName;
    private String randStr;
    private Long timestamp;


}
