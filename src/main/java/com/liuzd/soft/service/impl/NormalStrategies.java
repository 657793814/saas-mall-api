package com.liuzd.soft.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuzd.soft.annotation.StrategiesAnnotation;
import com.liuzd.soft.dao.PBuyerAddressDao;
import com.liuzd.soft.dao.PItemsDao;
import com.liuzd.soft.dao.PProductsDao;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.service.CalculateStrategies;
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
@StrategiesAnnotation(name = CalculateStrategiesFactory.STRATEGIES_NORMAL)
public class NormalStrategies extends CalculateStrategies {


    public NormalStrategies(PProductsDao productsDao, PItemsDao itemsDao, ObjectMapper objectMapper, PBuyerAddressDao pBuyerAddressDao) {
        super(productsDao, itemsDao, objectMapper, pBuyerAddressDao);
    }

    @Override
    public void initData() {
        super.initData();
        if (Objects.isNull(this.calculateParam)) {
            throw MyException.exception(RetEnums.FAIL, "参数异常");
        }
    }

    @Override
    public void calculate() {
        super.calculate();
        log.info("策略 {} 计算价格====> buyNum:{}, price:{}, total_price:{}", CalculateStrategiesFactory.STRATEGIES_NORMAL, this.calculateParam.getOrderItems().get(0).getBuyNum(), getPrice(), getPrice().multiply(BigDecimal.valueOf(this.calculateParam.getOrderItems().get(0).getBuyNum())));
    }
}
