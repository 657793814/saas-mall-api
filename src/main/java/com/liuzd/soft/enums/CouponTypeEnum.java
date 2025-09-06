package com.liuzd.soft.enums;

/**
 * @author: liuzd
 * @date: 2025/9/6
 * @email: liuzd2025@qq.com
 * @desc
 */
public enum CouponTypeEnum {

    PLATFORM_DISCOUNT_COUPON(1, "平台满减优惠券"),
    PLATFORM_SCALE_COUPON(2, "平台折扣优惠券"),
    PRODUCT_DISCOUNT_COUPON(3, "商品满减优惠券"),
    PRODUCT_SCALE_COUPON(4, "商品折扣优惠券");


    private int value;
    private String desc;

    CouponTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }
}
