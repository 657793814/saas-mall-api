package com.liuzd.soft.vo.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * @author: liuzd
 * @date: 2025/9/6
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
public class CreatePayReq {
    private String payType;
    private String orderNo;
}
