package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.PayStrategiesAnnotation;
import com.liuzd.soft.dao.PTradeDao;
import com.liuzd.soft.service.PayService;
import com.liuzd.soft.utils.IdUtils;
import com.liuzd.soft.vo.order.CreatePayReq;
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
@PayStrategiesAnnotation(name = PayStrategiesFactory.PAY_STRATEGIES_WEIXIN)
public class WxPayServiceImpl extends PayService {
    public WxPayServiceImpl(PTradeDao pTradeDao) {
        super(pTradeDao);
    }

    @Override
    public void createPay(CreatePayReq req) {
        this.payType = PayStrategiesFactory.PAY_STRATEGIES_WEIXIN;
        this.outTradeNo = "wx_" + IdUtils.generateOutTradeNo();
        super.createPay(req);
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
