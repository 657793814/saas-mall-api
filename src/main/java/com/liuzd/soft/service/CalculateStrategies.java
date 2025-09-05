package com.liuzd.soft.service;

import com.liuzd.soft.vo.strategies.CalculateParam;

/**
 * 计算策略
 *
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
public interface CalculateStrategies {

    void initData(CalculateParam calculateParam);

    void calculate();

}
