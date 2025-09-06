package com.liuzd.soft.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: liuzd
 * @date: 2025/9/6
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {

    private Integer couponId;
    private String couponName;
    private String couponDesc;
    private Integer couponType;
    private BigDecimal couponPrice;
    private BigDecimal scale;  //折扣率，eg:0.8
    private Set<Integer> skuIds;  //优惠券可用商品

    public CouponDto(Integer couponId, String couponName, String couponDesc, Integer couponType, BigDecimal price, BigDecimal scale) {
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponDesc = couponDesc;
        this.couponType = couponType;
        this.couponPrice = price;
        this.scale = scale;

        //todo mock data
        Set<Integer> skuIds = new HashSet<>();
        for (int i = 1; i < 10; i++) {
            skuIds.add(i);
        }
        this.skuIds = skuIds;

    }
}
