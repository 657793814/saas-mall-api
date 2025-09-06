package com.liuzd.soft.entity;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liuzd.soft.consts.GlobalConstant;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 支付交易表
 */
@Data
@DS(value = GlobalConstant.DEFAULT_DB_KEY)
@TableName("trade")
public class PTradeEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 内部交易号
     */
    @TableField("trade_no")
    private String tradeNo;

    /**
     * 外部交易号
     */
    @TableField("out_trade_no")
    private String outTradeNo;

    /**
     * 订单no
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 支付方式
     */
    @TableField("pay_type")
    private String payType;

    /**
     * 支付金额
     */
    @TableField("pay_money")
    private BigDecimal payMoney;

    /**
     * 支付状态，默认0代支付，1支付成功 2支付失败
     */
    @TableField("pay_state")
    private Integer payState;

    /**
     * 成功支付时间
     */
    @TableField("pay_time")
    private Timestamp payTime;

    /**
     * 创建支付单时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 超时支付时间，默认是创建时间+半小时
     */
    @TableField("out_pay_time")
    private Date outPayTime;
}
