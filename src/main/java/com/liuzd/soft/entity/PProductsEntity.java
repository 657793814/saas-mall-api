package com.liuzd.soft.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 中心库商品主表
 *
 * @author: liuzd
 * @date: 2025/8/24
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@TableName("products")
public class PProductsEntity {

    @TableId(value = "`id`", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "`tenant_code`")
    private String tenantCode;

    @TableField(value = "`code`")
    private String code;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "`desc`")
    private String desc;

    @TableField(value = "`detail`")
    private String detail;

    @TableField(value = "`img_urls`")
    private String imgUrls;

    @TableField(value = "`enable`")
    private Integer enable;

    @TableField(value = "`create_time`")
    private String createTime;

    @TableField(value = "`create_user`")
    private String createUser;

    @TableField(value = "`update_time`")
    private String updateTime;

    @TableField(value = "`update_user`")
    private String updateUser;

    @TableField(value = "`platform_status`")
    private Integer platformStatus;

    @TableField(value = "`one_category`")
    private Integer oneCategory;

    @TableField(value = "`two_category`")
    private Integer twoCategory;

    @TableField(value = "`three_category`")
    private Integer threeCategory;

    @TableField(value = "`shipping_template_id`")
    private Integer shippingTemplateId;


    @TableField(value = "`sale_num`")
    private Long saleNum;

}
