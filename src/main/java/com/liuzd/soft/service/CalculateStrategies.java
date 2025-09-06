package com.liuzd.soft.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuzd.soft.dao.PBuyerAddressDao;
import com.liuzd.soft.dao.PItemsDao;
import com.liuzd.soft.dao.PProductsDao;
import com.liuzd.soft.dto.PBuyerAddressDto;
import com.liuzd.soft.dto.coupon.CouponDto;
import com.liuzd.soft.entity.PBuyerAddressEntity;
import com.liuzd.soft.entity.PItemsEntity;
import com.liuzd.soft.entity.PProductsEntity;
import com.liuzd.soft.enums.CouponTypeEnum;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.vo.order.CalculateOrderItem;
import com.liuzd.soft.vo.product.SpecInfo;
import com.liuzd.soft.vo.strategies.CalculateParam;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算策略
 *
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
@Slf4j
@Data
@RequiredArgsConstructor
public class CalculateStrategies {


    public BigDecimal price = new BigDecimal(0);
    public CalculateParam calculateParam;

    final PProductsDao productsDao;
    final PItemsDao itemsDao;
    final ObjectMapper objectMapper;
    final PBuyerAddressDao pBuyerAddressDao;

    public void initData() {
        if (Objects.isNull(this.calculateParam)) {
            throw MyException.exception(RetEnums.FAIL, "订单参数异常");
        }
        if (this.calculateParam.getUid() == 0) {
            throw MyException.exception(RetEnums.FAIL, "用户参数异常");
        }

        handleUserAddress();
        handleUserCoupon();

    }

    /**
     * 计算价格 并填充 this.calculateParam
     */
    public void calculate() {
        //查询sku
        Set<Integer> skuIds = calculateParam.getOrderItems().stream().map(CalculateOrderItem::getSkuId).collect(Collectors.toSet());
        QueryWrapper<PItemsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", skuIds);
        List<PItemsEntity> skus = itemsDao.selectList(queryWrapper);
        Map<Integer, PItemsEntity> skuMap = skus.stream().collect(Collectors.toMap(PItemsEntity::getId, item -> item));

        //查询 product
        Set<Integer> productIds = skus.stream().map(PItemsEntity::getProductId).collect(Collectors.toSet());
        QueryWrapper<PProductsEntity> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.in("id", productIds);
        List<PProductsEntity> products = productsDao.selectList(queryWrapper2);
        Map<Integer, PProductsEntity> productMap = products.stream().collect(Collectors.toMap(PProductsEntity::getId, item -> item));


        calculateParam.getOrderItems().stream().forEach(item -> {

            //处理子单基础信息
            fillSkuInfo(skuMap.get(item.getSkuId()), productMap.get(item.getProductId()), item);

            //处理子单总价 单价*数量 和 累加以下整单的总价
            handleTotalPrice(item);

            //处理子单优惠金额
            handleCouponPrice(item, 1);

            //子单总支付金额
            handlePayFee(item);

            //处理总运费
            handleShippingPrice(item.getShippingPrice());

        });

        //处理总优惠金额
        handleCouponPrice(null, 2);

        //处理实际支付金额
        handlePayFee();

        log.info("计算结果：{}", calculateParam.toString());
    }

    /**
     * 填充一些基础信息
     *
     * @param sku
     */
    public void fillSkuInfo(PItemsEntity sku, PProductsEntity spu, CalculateOrderItem item) {

        //设置活动价格
        item.setTitle(spu.getName());
        item.setOriginalPrice(sku.getSalePrice());
        if (this.calculateParam.getGroupId() > 0 || this.calculateParam.getSecKillId() > 0) {
            item.setPrice(this.getPrice());
        } else {
            item.setPrice(sku.getSalePrice());
        }

        //处理规格数据
        try {
            item.setSpecInfo(objectMapper.readValue(sku.getSpecData(), new TypeReference<List<SpecInfo>>() {
            }));
        } catch (JsonProcessingException e) {
            log.error("获取sku信息失败", e);
        }


        //处理子单运费
        item.setShippingId(spu.getShippingTemplateId());
        item.setShippingPrice(getShippingPrice(item.getShippingId()));

    }

    /**
     * 处理用户收货地址
     */
    public void handleUserAddress() {
        log.info("handleUserAddress exec");

        //用户指定了收货地址
        if (Objects.nonNull(this.calculateParam.getAddressId())) {
            QueryWrapper<PBuyerAddressEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", this.calculateParam.getAddressId());
            queryWrapper.eq("uid", this.calculateParam.getUid());
            PBuyerAddressEntity entity = pBuyerAddressDao.selectOne(queryWrapper);
            if (Objects.nonNull(entity)) {
                this.calculateParam.setAddressDto(objectMapper.convertValue(entity, PBuyerAddressDto.class));
                return;
            }
            this.calculateParam.setAddressId(0);
        }

        //从已有地址选择一个
        QueryWrapper<PBuyerAddressEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", this.calculateParam.getUid());
        queryWrapper.orderByDesc("is_default").orderByDesc("id");
        List<PBuyerAddressEntity> addressList = pBuyerAddressDao.selectList(queryWrapper);
        if (CollUtil.isEmpty(addressList)) {
            return;
        }
        PBuyerAddressEntity address = addressList.get(0);
        this.calculateParam.setAddressId(address.getId());
        this.calculateParam.setAddressDto(objectMapper.convertValue(address, PBuyerAddressDto.class));
    }

    /**
     * 处理用户优惠券
     */
    public void handleUserCoupon() {
        log.info("handleUserCoupon exec");
        //todo

    }

    /**
     * 处理运费
     *
     * @param shippingPrice
     */

    public void handleShippingPrice(BigDecimal shippingPrice) {
        log.info("handleShippingPrice exec");
        this.calculateParam.setShippingPrice(this.calculateParam.getShippingPrice().add(shippingPrice));
    }

    /**
     * calculate()中调用
     * 如果是单品 则在 OrderItems 遍历时处理
     * 否则在 OrderItems 遍历完成后处理
     *
     * @param item
     * @param flag 1:处理单品 2:处理整个订单
     */
    public void handleCouponPrice(CalculateOrderItem item, int flag) {
        log.info("handleCouponPrice exec");
        if (Objects.isNull(this.calculateParam.getCouponId())) {
            return;
        }

        CouponDto coupon = getCoupon(this.calculateParam.getCouponId());
        if (Objects.isNull(coupon)) {
            return;
        }

        //todo 计算优惠券金额
        Integer couponType = coupon.getCouponType();
        if (flag == 2) {
            //平台满减针对整单处理，其他的都已经在子单中处理了
            if (couponType == CouponTypeEnum.PLATFORM_DISCOUNT_COUPON.getValue()) {
                BigDecimal couponPrice = this.calculateParam.getTotalPrice().min(coupon.getCouponPrice());
                this.calculateParam.setCouponPrice(couponPrice);

                //回填子单,按比例均摊
                BigDecimal totalPrice = this.calculateParam.getTotalPrice();
                BigDecimal couponPricePer = new BigDecimal(0);
                int size = this.calculateParam.getOrderItems().size();
                for (int i = 0; i < size; i++) {
                    if (couponPricePer.compareTo(couponPrice) >= 0) {
                        this.calculateParam.getOrderItems().get(i).setCouponPrice(new BigDecimal(0));
                        continue;
                    }
                    if (i == size - 1) {
                        this.calculateParam.getOrderItems().get(i).setCouponPrice(couponPrice.subtract(couponPricePer));
                    } else {
                        BigDecimal childrenTotalPrice = this.calculateParam.getOrderItems().get(i).getTotalPrice();
                        BigDecimal childrenCouponPrice = childrenTotalPrice.multiply(couponPrice).divide(totalPrice, 2, RoundingMode.HALF_UP);
                        couponPricePer = couponPricePer.add(childrenCouponPrice);
                        this.calculateParam.getOrderItems().get(i).setCouponPrice(childrenCouponPrice);
                    }
                }

            } else {
                //累计所有子单优惠金额
                this.calculateParam.getOrderItems().forEach(orderItem -> {
                    this.calculateParam.setCouponPrice(this.calculateParam.getCouponPrice().add(orderItem.getCouponPrice()));
                });
            }

        } else {
            BigDecimal scale = coupon.getScale();
            Set<Integer> skuIds = coupon.getSkuIds();

            //单品折扣
            if (couponType == CouponTypeEnum.PRODUCT_SCALE_COUPON.getValue()) {
                if (skuIds.contains(item.getSkuId())) {
                    item.setCouponPrice(item.getTotalPrice().multiply(new BigDecimal(1).subtract(scale)));
                }
                return;
            }

            //单品满减
            if (couponType == CouponTypeEnum.PRODUCT_DISCOUNT_COUPON.getValue()) {
                if (skuIds.contains(item.getSkuId())) {
                    item.setCouponPrice(item.getTotalPrice().min(coupon.getCouponPrice()));
                }
                return;
            }

            //平台折扣,先算单品，再整单累计
            if (couponType == CouponTypeEnum.PLATFORM_SCALE_COUPON.getValue()) {
                //平台折扣就不用管sku
                item.setCouponPrice(item.getTotalPrice().multiply(new BigDecimal(1).subtract(scale)));
            }
        }
    }

    /**
     * 实际商品总金额（不包括运费和优惠券）
     *
     * @param item
     */
    public void handleTotalPrice(CalculateOrderItem item) {
        log.info("handleTotalPrice exec");
        //计算totalPrice
        BigDecimal totalPrice = item.getPrice().multiply(new BigDecimal(item.getBuyNum()));
        item.setTotalPrice(totalPrice);
        this.calculateParam.setTotalPrice(this.calculateParam.getTotalPrice().add(totalPrice));
    }

    /**
     * 实际需要支付的总金额
     * 商品总价 + 总运费 - 总优惠券金额
     */
    public void handlePayFee() {
        log.info("handlePayFee exec");
        this.calculateParam.setPayFee(
                this.calculateParam.getTotalPrice().add(
                        this.calculateParam.getShippingPrice()).subtract(
                        this.calculateParam.getCouponPrice())

        );
    }

    public void handlePayFee(CalculateOrderItem item) {
        log.info("handlePayFee exec");
        item.setPayFee(
                item.getTotalPrice().add(
                        item.getShippingPrice()).subtract(
                        item.getCouponPrice())

        );
    }


    public BigDecimal getShippingPrice(Integer shippingId) {
        //todo mock data
        Map<Integer, BigDecimal> shippingPriceMap = new HashMap<Integer, BigDecimal>() {
            {
                put(1, new BigDecimal(10));
                put(2, new BigDecimal(5));
                put(3, new BigDecimal(0));
            }
        };
        return shippingPriceMap.getOrDefault(shippingId, BigDecimal.ZERO);
    }

    /**
     * 获取指定优惠券
     *
     * @param couponId
     * @return
     */
    public CouponDto getCoupon(Integer couponId) {
        //todo mock data
        Map<Integer, CouponDto> couponPriceMap = new HashMap<Integer, CouponDto>() {
            {
                put(1, new CouponDto(1, "满减券", "满100减10", CouponTypeEnum.PLATFORM_DISCOUNT_COUPON.getValue(), new BigDecimal(10), new BigDecimal(0)));
                put(2, new CouponDto(2, "折扣券", "满100打9.5折", CouponTypeEnum.PLATFORM_SCALE_COUPON.getValue(), new BigDecimal(0), new BigDecimal(0.8)));
            }
        };
        return couponPriceMap.get(couponId);
    }

}
