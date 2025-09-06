package com.liuzd.soft.api;

import com.liuzd.soft.annotation.NoLogin;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.service.CalculateStrategies;
import com.liuzd.soft.service.PayService;
import com.liuzd.soft.service.impl.CalculateStrategiesFactory;
import com.liuzd.soft.service.impl.PayStrategiesFactory;
import com.liuzd.soft.vo.ResultMessage;
import com.liuzd.soft.vo.order.CalculateOrderItem;
import com.liuzd.soft.vo.order.CreatePayReq;
import com.liuzd.soft.vo.strategies.CalculateParam;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
@RestController
@RequestMapping(path = "/test")
@RequiredArgsConstructor
@NoLogin
public class TestApi {

    @RequestMapping(path = "/index")
    public ResultMessage<Object> index(@RequestParam(value = "name", required = true, defaultValue = "") String name) {
        if (StringUtil.isBlank(name)) {
            return ResultMessage.fail(RetEnums.PARAMETER_NOT_VALID.getCode(), RetEnums.PARAMETER_NOT_VALID.getMessage());
        }
        CalculateStrategies strategies = CalculateStrategiesFactory.strategiesMap.get(CalculateStrategiesFactory.CALCULATE_STRATEGIES_PREFIX + name);
        if (ObjectUtils.isEmpty(strategies)) {
            return ResultMessage.fail(RetEnums.PARAMETER_NOT_VALID.getCode(), "策略不存在");
        }
        CalculateParam calculateParam = new CalculateParam();
        calculateParam.setUid(1);
        calculateParam.setGroupId(0);
        calculateParam.setSecKillId(0);
        calculateParam.setCouponId(1);
        calculateParam.setAddressId(1);

        CalculateOrderItem orderItem = new CalculateOrderItem();
        orderItem.setProductId(1);
        orderItem.setSkuId(1);
        orderItem.setBuyNum((int) Math.floor(Math.random() * 1000));

        CalculateOrderItem orderItem2 = new CalculateOrderItem();
        orderItem2.setProductId(1);
        orderItem2.setSkuId(2);
        orderItem2.setBuyNum((int) Math.floor(Math.random() * 1000));

        calculateParam.setOrderItems(Arrays.asList(orderItem, orderItem2));
        strategies.setCalculateParam(calculateParam);
        strategies.initData();
        strategies.calculate();
        return ResultMessage.success(strategies.calculateParam);
    }

    @RequestMapping(path = "/pay")
    public ResultMessage<Object> pay(@RequestParam(value = "name", required = true, defaultValue = "") String name) {
        if (StringUtil.isBlank(name)) {
            return ResultMessage.fail(RetEnums.PARAMETER_NOT_VALID.getCode(), RetEnums.PARAMETER_NOT_VALID.getMessage());
        }
        PayService payService = PayStrategiesFactory.strategiesMap.get(PayStrategiesFactory.PAY_STRATEGIES_PREFIX + name);
        if (ObjectUtils.isEmpty(payService)) {
            return ResultMessage.fail(RetEnums.PARAMETER_NOT_VALID.getCode(), "策略不存在");
        }
        CreatePayReq req = new CreatePayReq();
        req.setOrderNo("order_test_" + System.currentTimeMillis());
        req.setPayType(name);
        payService.createPay(req);
        return ResultMessage.success("exec success");
    }

}
