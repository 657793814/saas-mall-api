package com.liuzd.soft.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 订单表 DTO
 */
@Data
public class POrdersDto {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 父订单号，拆单订单会有父订单
     */
    private String parentOrderNo;

    private Integer uid;

    /**
     * 订单总金额
     */
    private BigDecimal orderTotal;

    /**
     * 运费总金额
     */
    private BigDecimal shippingFee;

    /**
     * 优惠总金额
     */
    private BigDecimal couponFee;

    /**
     * 实际商品总金额
     */
    private BigDecimal realProductFee;

    /**
     * 实际支付金额
     */
    private BigDecimal payFee;

    /**
     * 订单数据json
     */
    private String orderData;

    /**
     * 订单创建时间
     */
    private Timestamp createTime;

    /**
     * 订单归属商户，拆单的子订单有值
     */
    private String tenantCode;

    /**
     * 订单状态 默认0初始。1订单已确认 待发货，2订单已发货待收货 ，3确认收货交易完成，99：用户取消订单，订单关闭. 100商家取消订单，订单关闭
     */
    private Integer orderState;

    /**
     * 订单支付状态 0待支付，1成功支付
     */
    private Integer payState;

    /**
     * 支付时间
     */
    private Timestamp payTime;
}
