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
public class CreatePayResp<T> {
    private Integer payType;
    private String orderNo;
    private String tradeNo; //内部交易单号
    private T extra;  //第三方交易模型
}
