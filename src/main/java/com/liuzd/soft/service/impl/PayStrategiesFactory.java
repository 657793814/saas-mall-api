package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.PayStrategiesAnnotation;
import com.liuzd.soft.service.PayService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
@Component
@Slf4j
public class PayStrategiesFactory {

    //策略key定义
    final static String PAY_STRATEGIES_ACCOUNT = "account";   //本地钱包
    final static String PAY_STRATEGIES_WEIXIN = "weixin";   //微信
    final static String PAY_STRATEGIES_ZHIFUBAO = "zhifubao";  //支付宝

    private List<PayService> strategieList;

    public static final String PAY_STRATEGIES_PREFIX = "PAY_STRATEGIES_PREFIX_";

    public static final Map<String, PayService> strategiesMap = new HashMap<>();

    @Autowired
    public PayStrategiesFactory(List<PayService> objList) {
        this.strategieList = objList;
    }

    @PostConstruct
    public void init() {
        for (PayService pay : strategieList) {
            Class<?> targetClass = AopUtils.getTargetClass(pay);
            PayStrategiesAnnotation annotationInClass = AnnotationUtils.findAnnotation(targetClass, PayStrategiesAnnotation.class);
            if (annotationInClass == null) {
                log.error("目标类:{} 缺少注解", targetClass.getName());
                continue;
            }
            String strategyName = annotationInClass.name();
            if ("".equals(strategyName)) {
                log.error("目标类:{} 缺少注解 name 值", targetClass.getName());
                continue;
            }
            log.info("目标策略类:{} 加载成功", targetClass.getName());
            strategiesMap.put(PAY_STRATEGIES_PREFIX + strategyName, pay);
        }
    }
}
