package com.liuzd.soft.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuzd.soft.entity.PBuyerAddressEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商城用户收货地址DAO接口
 */
@Mapper
public interface PBuyerAddressDao extends BaseMapper<PBuyerAddressEntity> {
}
