package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.PayStrategiesAnnotation;
import com.liuzd.soft.dao.POrdersDao;
import com.liuzd.soft.dao.PTradeDao;
import com.liuzd.soft.service.PayService;
import com.liuzd.soft.utils.IdUtils;
import com.liuzd.soft.vo.order.CreatePayReq;
import com.liuzd.soft.vo.order.CreatePayResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 本地钱包支付
 *
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@PayStrategiesAnnotation(name = PayStrategiesFactory.PAY_STRATEGIES_ACCOUNT)
public class AccountPayServiceImpl extends PayService {

    public AccountPayServiceImpl(PTradeDao pTradeDao, POrdersDao pOrdersDao) {
        super(pTradeDao, pOrdersDao);
    }

    @Override
    public CreatePayResp createPay(CreatePayReq req) {
        this.payType = PayStrategiesFactory.PAY_STRATEGIES_ACCOUNT;
        this.outTradeNo = "my_" + IdUtils.generateOutTradeNo();
        return super.createPay(req);
    }

    @Override
    public void doPay(String tradeNo) {
        log.info("account doPay");
    }

    @Override
    public void queryPay(String tradeNo) {
        log.info("account queryPay");
    }

}
