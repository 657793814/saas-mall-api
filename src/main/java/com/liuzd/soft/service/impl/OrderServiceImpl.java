package com.liuzd.soft.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuzd.soft.consts.GlobalConstant;
import com.liuzd.soft.context.ThreadContextHolder;
import com.liuzd.soft.dao.POrdersDao;
import com.liuzd.soft.dao.PProductsDao;
import com.liuzd.soft.dto.token.TokenInfo;
import com.liuzd.soft.entity.POrdersEntity;
import com.liuzd.soft.entity.PProductsEntity;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.service.OrderService;
import com.liuzd.soft.utils.DateUtils;
import com.liuzd.soft.utils.IdUtils;
import com.liuzd.soft.vo.order.CalculateOrderItem;
import com.liuzd.soft.vo.order.CreateOrderResp;
import com.liuzd.soft.vo.order.PayTypeInfo;
import com.liuzd.soft.vo.strategies.CalculateParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: liuzd
 * @date: 2025/9/6
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    final POrdersDao ordersDao;
    final ObjectMapper objectMapper;
    final PProductsDao pProductsDao;

    @Override
    public CreateOrderResp createOrder(CalculateParam calculateParam) throws JsonProcessingException {

        String tenantCode = "";
        Map<Integer, String> tenantCodeMap;
        if (calculateParam.getOrderItems().size() == 1) {
            tenantCodeMap = new HashMap<>();
            Integer productId = calculateParam.getOrderItems().get(0).getProductId();
            PProductsEntity spu = pProductsDao.selectById(productId);
            tenantCode = spu.getTenantCode();
        } else {
            Set<Integer> productIds = calculateParam.getOrderItems().stream().map(CalculateOrderItem::getProductId).collect(Collectors.toSet());
            QueryWrapper<PProductsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", productIds);
            List<PProductsEntity> products = pProductsDao.selectList(queryWrapper);
            tenantCodeMap = products.stream().collect(Collectors.toMap(PProductsEntity::getId, PProductsEntity::getTenantCode));
        }

        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);


        POrdersEntity ordersEntity = new POrdersEntity();
        String orderNo = IdUtils.generateOrderNo();
        ordersEntity.setOrderNo(orderNo);
        ordersEntity.setParentOrderNo("");
        ordersEntity.setUid(tokenInfo.getUid());
        ordersEntity.setRealProductFee(calculateParam.getTotalPrice());
        ordersEntity.setShippingFee(calculateParam.getShippingPrice());
        ordersEntity.setCouponFee(calculateParam.getCouponPrice());
        ordersEntity.setPayFee(calculateParam.getPayFee());
        ordersEntity.setOrderTotal(calculateParam.getTotalPrice().add(calculateParam.getShippingPrice()));
        ordersEntity.setOrderData(objectMapper.writeValueAsString(calculateParam));
        ordersEntity.setCreateTime(DateUtils.getCurrentDate());
        ordersEntity.setTenantCode(tenantCode);
        ordersEntity.setOrderState(0);
        ordersEntity.setPayState(0);
        ordersDao.insert(ordersEntity);

        if (calculateParam.getOrderItems().size() > 1) {
            calculateParam.getOrderItems().forEach(item -> {
                POrdersEntity ordersEntity2 = new POrdersEntity();
                String childrenOrderNo = IdUtils.generateOrderNo();
                ordersEntity2.setOrderNo(childrenOrderNo);
                ordersEntity2.setParentOrderNo(orderNo);
                ordersEntity2.setUid(tokenInfo.getUid());
                ordersEntity2.setRealProductFee(item.getTotalPrice());
                ordersEntity2.setShippingFee(item.getShippingPrice());
                ordersEntity2.setCouponFee(item.getCouponPrice());
                ordersEntity2.setPayFee(item.getPayFee());
                ordersEntity2.setOrderTotal(item.getTotalPrice().add(item.getShippingPrice()));
                try {
                    ordersEntity2.setOrderData(objectMapper.writeValueAsString(item));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                ordersEntity2.setCreateTime(DateUtils.getCurrentDate());
                ordersEntity2.setTenantCode(tenantCodeMap.get(item.getProductId()));
                ordersEntity2.setOrderState(0);
                ordersEntity2.setPayState(0);
                ordersDao.insert(ordersEntity2);

            });
        }

        CreateOrderResp resp = new CreateOrderResp();
        resp.setOrderNo(orderNo);
        resp.setCalculateParam(calculateParam);
        resp.setPayTypeInfos(getPayTypeInfos());
        return resp;
    }

    @Override
    public CreateOrderResp payDetail(String orderNo) throws JsonProcessingException {
        TokenInfo tokenInfo = (TokenInfo) ThreadContextHolder.get(GlobalConstant.LOGIN_USER_INFO);

        QueryWrapper<POrdersEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        queryWrapper.eq("uid", tokenInfo.getUid());
        POrdersEntity ordersEntity = ordersDao.selectOne(queryWrapper);
        Assert.notNull(ordersEntity, () -> MyException.exception(RetEnums.ORDER_NOT_EXIST));

        CreateOrderResp resp = new CreateOrderResp();
        resp.setOrderNo(orderNo);
        resp.setCalculateParam(objectMapper.readValue(ordersEntity.getOrderData(), CalculateParam.class));
        resp.setPayTypeInfos(getPayTypeInfos());
        return resp;


    }

    private List<PayTypeInfo> getPayTypeInfos() {
        return new ArrayList<PayTypeInfo>() {{
            add(new PayTypeInfo(PayStrategiesFactory.PAY_STRATEGIES_ACCOUNT, "账户钱包"));
            add(new PayTypeInfo(PayStrategiesFactory.PAY_STRATEGIES_ZHIFUBAO, "支付宝"));
            add(new PayTypeInfo(PayStrategiesFactory.PAY_STRATEGIES_WEIXIN, "微信"));
        }};
    }

}
