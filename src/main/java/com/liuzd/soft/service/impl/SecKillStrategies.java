package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.StrategiesAnnotation;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.service.CalculateStrategies;
import com.liuzd.soft.vo.strategies.CalculateParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author: liuzd
 * @date: 2025/9/5
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@Data
@StrategiesAnnotation(name = CalculateStrategiesFactory.STRATEGIES_SEC_KILL)
public class SecKillStrategies implements CalculateStrategies {

    protected BigDecimal price;
    protected CalculateParam calculateParam;

    @Override
    public void initData(CalculateParam calculateParam) {
        //todo 设置价格
        this.calculateParam = calculateParam;
        if (Objects.isNull(this.calculateParam)) {
            throw MyException.exception(RetEnums.FAIL, "参数异常");
        }
        if (Objects.isNull(this.calculateParam.getSecKillId())) {
            throw MyException.exception(RetEnums.FAIL, "参数异常");
        }
        setPrice(BigDecimal.valueOf(88));
    }

    @Override
    public void calculate() {
        log.info("策略 {} 计算价格====> buyNum:{}, price:{}, total_price:{}", CalculateStrategiesFactory.STRATEGIES_SEC_KILL, this.calculateParam.getBuyNum(), getPrice(), getPrice().doubleValue() * this.calculateParam.getBuyNum());
    }
}
