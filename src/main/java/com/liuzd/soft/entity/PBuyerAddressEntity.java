package com.liuzd.soft.entity;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liuzd.soft.consts.GlobalConstant;
import lombok.Data;

/**
 * 商城用户收货地址管理表
 */
@Data
@DS(value = GlobalConstant.DEFAULT_DB_KEY)
@TableName("buyer_address")
public class PBuyerAddressEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * buyer表id
     */
    private Integer uid;

    private String name;
    private String mobile;

    /**
     * 省份代码
     */
    private Integer province;

    /**
     * 城市代码
     */
    private Integer city;

    /**
     * 区县代码
     */
    private Integer district;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 是否默认地址 0-否 1-是
     */
    @TableField("is_default")
    private Integer isDefault;
}
