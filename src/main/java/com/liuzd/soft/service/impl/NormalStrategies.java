package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.StrategiesAnnotation;
import com.liuzd.soft.service.CalculateStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@StrategiesAnnotation(name = CalculateStrategiesFactory.STRATEGIES_NORMAL)
public class NormalStrategies implements CalculateStrategies {

    @Override
    public void calculate() {
        log.info("策略 {} 计算价格 =============>", CalculateStrategiesFactory.STRATEGIES_NORMAL);
    }
}
