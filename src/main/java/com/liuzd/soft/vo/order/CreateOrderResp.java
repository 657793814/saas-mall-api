package com.liuzd.soft.vo.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.liuzd.soft.vo.strategies.CalculateParam;
import lombok.Data;

import java.util.List;

/**
 * @author: liuzd
 * @date: 2025/9/6
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
public class CreateOrderResp {

    private String orderNo;
    private CalculateParam calculateParam;
    private List<PayTypeInfo> payTypeInfos;
}
