package com.liuzd.soft.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuzd.soft.vo.order.CreateOrderResp;
import com.liuzd.soft.vo.strategies.CalculateParam;

/**
 * @author: liuzd
 * @date: 2025/9/6
 * @email: liuzd2025@qq.com
 * @desc
 */
public interface OrderService {

    CreateOrderResp createOrder(CalculateParam calculateParam) throws JsonProcessingException;

    CreateOrderResp payDetail(String orderNo) throws JsonProcessingException;
}
