package com.liuzd.soft.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liuzd.soft.consts.GlobalConstant;
import com.liuzd.soft.context.ThreadContextHolder;
import com.liuzd.soft.dao.POrdersDao;
import com.liuzd.soft.dao.PTradeDao;
import com.liuzd.soft.dto.token.TokenInfo;
import com.liuzd.soft.entity.POrdersEntity;
import com.liuzd.soft.entity.PTradeEntity;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.utils.DateUtils;
import com.liuzd.soft.utils.IdUtils;
import com.liuzd.soft.vo.order.CreatePayReq;
import com.liuzd.soft.vo.order.CreatePayResp;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class PayService<T> {

    public String payType;
    public String tradeNo; //内部交易单号，内部交易唯一凭证
    public String outTradeNo; //外部交易号，外部交易唯一凭证
    public T extra; //第三方支付扩展数据


    final PTradeDao pTradeDao;
    final POrdersDao pOrdersDao;


    /**
     * 创建支付单
     */
    public CreatePayResp createPay(CreatePayReq req) {

        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);
        QueryWrapper<POrdersEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", req.getOrderNo());
        queryWrapper.eq("uid", tokenInfo.getUid());
        POrdersEntity ordersEntity = pOrdersDao.selectOne(queryWrapper);
        Assert.notNull(ordersEntity, () -> MyException.exception(RetEnums.ORDER_NOT_EXIST));

        //查询订单是否有支付单
        QueryWrapper<PTradeEntity> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("order_no", req.getOrderNo());
        PTradeEntity tradeEntity = pTradeDao.selectOne(queryWrapper2);
        if (Objects.nonNull(tradeEntity)) {
            if (tradeEntity.getPayState() == 1) {
                throw MyException.exception(RetEnums.BUSINESS_ERROR, "订单已支付");
            }
            if (tradeEntity.getPayState() == 0) {
                CreatePayResp resp = new CreatePayResp();
                resp.setPayType(tradeEntity.getPayType());
                resp.setOrderNo(tradeEntity.getOrderNo());
                resp.setTradeNo(tradeEntity.getTradeNo());
                resp.setExtra(null);
                return resp;
            }
        }

        tradeNo = IdUtils.generateTradeNo();
        PTradeEntity pTradeEntity = new PTradeEntity();
        pTradeEntity.setTradeNo(tradeNo);
        pTradeEntity.setOutTradeNo(req.getOrderNo());
        pTradeEntity.setOrderNo(req.getOrderNo());
        pTradeEntity.setPayType(req.getPayType());
        pTradeEntity.setPayMoney(ordersEntity.getPayFee());
        pTradeEntity.setPayState(0);
        pTradeEntity.setCreateTime(DateUtils.getCurrentDate());
        pTradeEntity.setOutPayTime(DateUtils.getFormatDate(1800));  //半个小时后超时
        pTradeDao.insert(pTradeEntity);
        log.info("创建支付单. payType:{}, tradeNo:{} , outTradeNo:{}", this.payType, this.tradeNo, this.outTradeNo);

        CreatePayResp resp = new CreatePayResp();
        resp.setPayType(req.getPayType());
        resp.setOrderNo(req.getOrderNo());
        resp.setTradeNo(this.tradeNo);
        resp.setExtra(null);
        return resp;
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
