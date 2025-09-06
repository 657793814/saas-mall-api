package com.liuzd.soft.service;

import com.liuzd.soft.dao.PTradeDao;
import com.liuzd.soft.utils.IdUtils;
import com.liuzd.soft.vo.order.CreatePayReq;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class PayService {

    public String payType;
    public String tradeNo; //内部交易单号，内部交易唯一凭证
    public String outTradeNo; //外部交易号，外部交易唯一凭证

    final PTradeDao pTradeDao;


    /**
     * 创建支付单
     */
    public void createPay(CreatePayReq req) {
        tradeNo = "no_" + IdUtils.generateTradeNo();
        log.info("创建支付单. payType:{}, tradeNo:{} , outTradeNo:{}", this.payType, this.tradeNo, this.outTradeNo);
    }

    /**
     * 执行支付
     *
     * @param tradeNo 内部交易单号
     */
    public void doPay(String tradeNo) {
    }

    /**
     * 查询支付状态
     *
     * @param tradeNo 内部交易单号
     */
    public void queryPay(String tradeNo) {
    }

}
