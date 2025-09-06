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
 * 订单表
 */
@Data
@DS(value = GlobalConstant.DEFAULT_DB_KEY)
@TableName("orders")
public class POrdersEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 父订单号，拆单订单会有父订单
     */
    @TableField("parent_order_no")
    private String parentOrderNo;

    @TableField("uid")
    private Integer uid;

    /**
     * 订单总金额
     */
    @TableField("order_total")
    private BigDecimal orderTotal;

    /**
     * 运费总金额
     */
    @TableField("shipping_fee")
    private BigDecimal shippingFee;

    /**
     * 优惠总金额
     */
    @TableField("coupon_fee")
    private BigDecimal couponFee;

    /**
     * 实际商品总金额
     */
    @TableField("real_product_fee")
    private BigDecimal realProductFee;

    /**
     * 实际支付金额
     */
    @TableField("pay_fee")
    private BigDecimal payFee;

    /**
     * 订单数据json
     */
    @TableField("order_data")
    private String orderData;

    /**
     * 订单创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 订单归属商户，拆单的子订单有值
     */
    @TableField("tenant_code")
    private String tenantCode;

    /**
     * 订单状态 默认0初始。1订单已确认 待发货，2订单已发货待收货 ，3确认收货交易完成，99：用户取消订单，订单关闭. 100商家取消订单，订单关闭
     */
    @TableField("order_state")
    private Integer orderState;

    /**
     * 订单支付状态 0待支付，1成功支付
     */
    @TableField("pay_state")
    private Integer payState;

    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Timestamp payTime;
}
