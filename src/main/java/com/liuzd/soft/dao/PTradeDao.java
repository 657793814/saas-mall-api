package com.liuzd.soft.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuzd.soft.entity.PTradeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付交易表 DAO接口
 */
@Mapper
public interface PTradeDao extends BaseMapper<PTradeEntity> {

}
