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
@StrategiesAnnotation(name = CalculateStrategiesFactory.STRATEGIES_GROUP)
public class GroupStrategies extends CalculateStrategies {


    public GroupStrategies(PProductsDao productsDao, PItemsDao itemsDao, ObjectMapper objectMapper, PBuyerAddressDao pBuyerAddressDao) {
        super(productsDao, itemsDao, objectMapper, pBuyerAddressDao);
    }

    @Override
    public void initData() {
        super.initData();
        if (Objects.isNull(this.calculateParam.getGroupId())) {
            throw MyException.exception(RetEnums.FAIL, "团购参数异常");
        }
        if (Objects.isNull(this.calculateParam.getOrderItems())) {
            throw MyException.exception(RetEnums.FAIL, "团购参数异常");
        }
        if (this.calculateParam.getOrderItems().size() > 1) {
            throw MyException.exception(RetEnums.FAIL, "团购参数异常");
        }
        //todo
        super.setPrice(BigDecimal.valueOf(99));
    }

    @Override
    public void calculate() {
        log.info("策略 {} 计算价格====> buyNum:{}, price:{}, total_price:{}", CalculateStrategiesFactory.STRATEGIES_GROUP, this.calculateParam.getOrderItems().get(0).getBuyNum(), getPrice(), getPrice().doubleValue() * this.calculateParam.getOrderItems().get(0).getBuyNum());
        super.calculate();
    }
}
