package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.PayStrategiesAnnotation;
import com.liuzd.soft.service.PayService;
import com.liuzd.soft.utils.IdUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信支付
 *
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@Data
@PayStrategiesAnnotation(name = PayStrategiesFactory.PAY_STRATEGIES_WEIXIN)
public class WxPayServiceImpl extends PayService {
    @Override
    public void createPay() {
        this.payType = PayStrategiesFactory.PAY_STRATEGIES_WEIXIN;
        this.outTradeNo = "wx_" + IdUtils.generateOutTradeNo();
        super.createPay();
    }

    @Override
    public void doPay(String tradeNo) {
        log.info("weixin doPay");
    }

    @Override
    public void queryPay(String tradeNo) {
        log.info("weixin queryPay");
    }
}
