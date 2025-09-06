package com.liuzd.soft.vo.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.liuzd.soft.vo.product.SpecInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
public class CalculateOrderItem {

    //此出数据由前端计算价格时提交
    private Integer productId;  //商品id
    private Integer skuId;  //skuId
    private Integer buyNum;  //购买数量
    private String title; //商品标题


    //以下数据在计算价格时候填充或修改
    private BigDecimal price;  //商品实际支付单价，考虑促销活动价格有变动
    private BigDecimal originalPrice; //商品原价格

    private Integer shippingId;  //运费模板id
    private BigDecimal shippingPrice = new BigDecimal(0);  //运费价格

    private List<SpecInfo> specInfo;  //商品规格数据
    private Integer couponId = 0;  //优惠券id
    private BigDecimal couponPrice = new BigDecimal(0); //优惠券金额

    //子单商品总金额 totalPrice = price * buyNum
    private BigDecimal totalPrice = new BigDecimal(0);

    //计算本单的支付金额 payFee = totalPrice + shippingPrice - couponPrice
    private BigDecimal payFee = new BigDecimal(0);

}
