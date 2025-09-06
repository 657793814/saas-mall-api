package com.liuzd.soft.vo.order;

import lombok.Data;

/**
 * @author: liuzd
 * @date: 2025/9/6
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
public class PayTypeInfo {
    private String payType;
    private String payTypeName;

    public PayTypeInfo(String payType, String payTypeName) {
        this.payType = payType;
        this.payTypeName = payTypeName;
    }
}
