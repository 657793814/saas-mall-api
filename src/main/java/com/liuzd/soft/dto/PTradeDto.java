package com.liuzd.soft.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 支付交易表 DTO
 */
@Data
public class PTradeDto {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 内部交易号
     */
    private String tradeNo;

    /**
     * 外部交易号
     */
    private String outTradeNo;

    /**
     * 订单no
     */
    private String orderNo;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 支付状态，默认0代支付，1支付成功 2支付失败
     */
    private Integer payState;

    /**
     * 成功支付时间
     */
    private Timestamp payTime;

    /**
     * 创建支付单时间
     */
    private Timestamp createTime;

    /**
     * 超时支付时间，默认是创建时间+半小时
     */
    private Timestamp outPayTime;
}
