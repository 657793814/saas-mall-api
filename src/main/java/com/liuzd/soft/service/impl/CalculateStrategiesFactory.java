package com.liuzd.soft.service.impl;

import com.liuzd.soft.annotation.StrategiesAnnotation;
import com.liuzd.soft.service.CalculateStrategies;
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
public class CalculateStrategiesFactory {

    //策略key定义
    final static String STRATEGIES_NORMAL = "normal";  //普通
    final static String STRATEGIES_GROUP = "group";  //团购
    final static String STRATEGIES_SEC_KILL = "sec_kill";  //秒杀

    private List<CalculateStrategies> strategieList;

    public static final String CALCULATE_STRATEGIES_PREFIX = "CALCULATE_STRATEGIES_PREFIX_";

    public static final Map<String, CalculateStrategies> strategiesMap = new HashMap<>();

    @Autowired
    public CalculateStrategiesFactory(List<CalculateStrategies> objList) {
        this.strategieList = objList;
    }

    @PostConstruct
    public void init() {
        for (CalculateStrategies strategies : strategieList) {
            Class<?> targetClass = AopUtils.getTargetClass(strategies);

            //1.获取目标类上的目标注解（可判断目标类是否存在该注解）
            StrategiesAnnotation annotationInClass = AnnotationUtils.findAnnotation(targetClass, StrategiesAnnotation.class);
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
            strategiesMap.put(CALCULATE_STRATEGIES_PREFIX + strategyName, strategies);
        }
    }
}
