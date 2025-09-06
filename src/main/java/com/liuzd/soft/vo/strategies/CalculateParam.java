package com.liuzd.soft.vo.strategies;

import com.liuzd.soft.dto.PBuyerAddressDto;
import com.liuzd.soft.vo.order.CalculateOrderItem;
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
public class CalculateParam {

    private Integer uid = 0;
    private Integer groupId = 0;  //团购活动id
    private Integer secKillId = 0; //秒杀活动id

    /**
     * 收货地址数据
     */
    private Integer addressId;  //收货地址id
    private PBuyerAddressDto addressDto;

    private List<CalculateOrderItem> orderItems;

    /**
     * 总运费
     */
    private BigDecimal shippingPrice = new BigDecimal(0);
    ;
    /**
     * 实际商品需要支付金额
     * 累加 orderItems 子项的 totalPrice
     */
    private BigDecimal totalPrice = new BigDecimal(0);

    /**
     * 优惠券 一个订单限制一张，orderItems也有这个字段
     * 类型：1. 平台累计满减类优惠券，2. 平台折扣类优惠券，3. 单品满减类优惠券,4. 单品折扣类优惠券
     * 如果时单品优惠券，则此处的数据记录到 orderItems具体的商品单上面
     * 如果是平台优惠券，则此处的优惠平摊到orderItems记录上
     */
    private Integer couponId = 0;  //优惠券id
    /**
     * 总的优惠券金额
     * 平台满减和折扣都是基于 totalPrice
     * 单品满减类和折扣都是基于 orderItems 子项的 couponPrice
     */
    private BigDecimal couponPrice = new BigDecimal(0);

    /**
     * 实际总共需要支付金额（包括运费在内）
     * payFee = totalPrice - couponPrice + shippingPrice
     */
    private BigDecimal payFee = new BigDecimal(0);
    ;

}
