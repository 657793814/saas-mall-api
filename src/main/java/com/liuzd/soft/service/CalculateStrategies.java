package com.liuzd.soft.service;

import com.liuzd.soft.vo.strategies.CalculateParam;

import java.math.BigDecimal;

/**
 * 计算策略
 *
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
public interface CalculateStrategies {

    BigDecimal price = null;
    CalculateParam calculateParam = null;  //计算价格相关数据

    void initData(CalculateParam calculateParam);

    void calculate();

}
