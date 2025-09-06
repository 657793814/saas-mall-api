package com.liuzd.soft.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuzd.soft.consts.GlobalConstant;
import com.liuzd.soft.context.ThreadContextHolder;
import com.liuzd.soft.dto.token.TokenInfo;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.service.CalculateStrategies;
import com.liuzd.soft.service.OrderService;
import com.liuzd.soft.service.PayService;
import com.liuzd.soft.service.impl.CalculateStrategiesFactory;
import com.liuzd.soft.service.impl.PayStrategiesFactory;
import com.liuzd.soft.vo.ResultMessage;
import com.liuzd.soft.vo.order.CreateOrderResp;
import com.liuzd.soft.vo.order.CreatePayReq;
import com.liuzd.soft.vo.order.CreatePayResp;
import com.liuzd.soft.vo.strategies.CalculateParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 订单相关api
 *
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
@RestController
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class OrderApi {

    final OrderService orderService;


    public String getCalculateStrategies(CalculateParam calculateParam) {
        if (calculateParam.getSecKillId() > 0) {
            return CalculateStrategiesFactory.CALCULATE_STRATEGIES_PREFIX + CalculateStrategiesFactory.STRATEGIES_SEC_KILL;
        }
        if (calculateParam.getGroupId() > 0) {
            return CalculateStrategiesFactory.CALCULATE_STRATEGIES_PREFIX + CalculateStrategiesFactory.STRATEGIES_GROUP;
        }
        return CalculateStrategiesFactory.CALCULATE_STRATEGIES_PREFIX + CalculateStrategiesFactory.STRATEGIES_NORMAL;
    }

    /**
     * 计算价格
     *
     * @return
     */
    @PostMapping(path = "/computer")
    public ResultMessage<CalculateParam> computer(@RequestBody CalculateParam calculateParam) {
        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);
        calculateParam.setUid(tokenInfo.getUid());
        CalculateStrategies calculateStrategies = CalculateStrategiesFactory.strategiesMap.get(getCalculateStrategies(calculateParam));
        if (Objects.isNull(calculateStrategies)) {
            return ResultMessage.fail(RetEnums.PARAMETER_NOT_VALID.getCode(), "参数错误");
        }
        //todo
        calculateParam.setCouponId(1);
        calculateStrategies.setCalculateParam(calculateParam);
        calculateStrategies.initData();
        calculateStrategies.calculate();
        return ResultMessage.success(calculateStrategies.getCalculateParam());
    }

    /**
     * 确认订单
     * 创建订单信息
     * 返回订单数+支付方式数据
     *
     * @return
     */
    @PostMapping(path = "/confirm")
    public ResultMessage<CreateOrderResp> confirm(@RequestBody CalculateParam calculateParam) throws JsonProcessingException {
        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);
        calculateParam.setUid(tokenInfo.getUid());
        CalculateStrategies calculateStrategies = CalculateStrategiesFactory.strategiesMap.get(getCalculateStrategies(calculateParam));
        if (Objects.isNull(calculateStrategies)) {
            return ResultMessage.fail(RetEnums.PARAMETER_NOT_VALID.getCode(), "参数错误");
        }
        //todo
        calculateParam.setCouponId(1);
        calculateStrategies.setCalculateParam(calculateParam);
        calculateStrategies.initData();
        calculateStrategies.calculate();

        //创建订单
        return ResultMessage.success(orderService.createOrder(calculateParam));
    }

    @RequestMapping("/pay_detail")
    public ResultMessage<CreateOrderResp> payDetail(@RequestParam("orderNo") String orderNo) throws JsonProcessingException {
        return ResultMessage.success(orderService.payDetail(orderNo));
    }

    /**
     * 支付订单
     * 根据支付方式创建支付单
     * 返回支付信息给前端，例如 微信支付jsapi参数等，本地钱包支付唤起支付密码输入等
     *
     * @return
     */
    @PostMapping(path = "/create_pay")
    public ResultMessage<CreatePayResp> createPay(@RequestBody CreatePayReq createPayReq) {

        PayService payService = PayStrategiesFactory.strategiesMap.get(PayStrategiesFactory.PAY_STRATEGIES_PREFIX + createPayReq.getPayType());
        if (Objects.isNull(payService)) {
            return ResultMessage.fail(RetEnums.PARAMETER_NOT_VALID.getCode(), "支付方式在暂不支持");
        }
        payService.createPay(createPayReq);
        return ResultMessage.success(null);
    }

    /**
     * 确认支付
     * 1. 本地账户支付，服务端验证支付密码处理支付确认
     * 2. 微信唤起支付 前端确认支付后调用，服务端查询微信api处理支付确认
     * 3. 支付宝唤起支付 前端确认支付后调用，服务端查询支付宝api处理支付确认
     * 等等
     *
     * @return
     */
    @PostMapping(path = "/do_pay")
    public ResultMessage<Object> doPay() {
        return ResultMessage.success("success");
    }


}
