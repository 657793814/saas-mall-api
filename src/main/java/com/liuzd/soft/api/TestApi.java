package com.liuzd.soft.api;

import com.liuzd.soft.annotation.NoLogin;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.service.CalculateStrategies;
import com.liuzd.soft.service.impl.CalculateStrategiesFactory;
import com.liuzd.soft.vo.ResultMessage;
import com.liuzd.soft.vo.strategies.CalculateParam;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        calculateParam.setProductId(1);
        calculateParam.setSkuId(1);
        calculateParam.setGroupId(1);
        calculateParam.setSecKillId(1);
        calculateParam.setBuyNum((int) Math.floor(Math.random() * 1000));
        strategies.initData(calculateParam);
        strategies.calculate();
        return ResultMessage.success("exec success");
    }

}
