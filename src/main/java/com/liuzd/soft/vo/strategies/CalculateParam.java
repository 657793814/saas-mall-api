package com.liuzd.soft.vo.strategies;

import lombok.Data;

/**
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
public class CalculateParam {

    private Integer productId;  //商品id
    private Integer skuId;  //skuId
    private Integer buyNum;  //购买数量
    private Integer groupId;  //团购活动id
    private Integer secKillId; //秒杀活动id

}
