package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.PayStrategiesAnnotation;
import com.liuzd.soft.dao.PTradeDao;
import com.liuzd.soft.service.PayService;
import com.liuzd.soft.utils.IdUtils;
import com.liuzd.soft.vo.order.CreatePayReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝支付
 *
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@PayStrategiesAnnotation(name = PayStrategiesFactory.PAY_STRATEGIES_ZHIFUBAO)
public class ZfbPayServiceImpl extends PayService {
    public ZfbPayServiceImpl(PTradeDao pTradeDao) {
        super(pTradeDao);
    }

    @Override
    public void createPay(CreatePayReq req) {
        this.payType = PayStrategiesFactory.PAY_STRATEGIES_ZHIFUBAO;
        this.outTradeNo = "zfb_" + IdUtils.generateOutTradeNo();
        super.createPay(req);
    }

    @Override
    public void doPay(String tradeNo) {
        log.info("zhifubao doPay");
    }

    @Override
    public void queryPay(String tradeNo) {
        log.info("zhifubao queryPay");
    }
}
