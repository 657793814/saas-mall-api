package com.liuzd.soft.dto;

import com.liuzd.soft.entity.PBuyerAddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商城用户收货地址DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PBuyerAddressDto {

    /**
     * 主键ID
     */
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
    private Integer isDefault;

    public PBuyerAddressDto(PBuyerAddressEntity pBuyerAddressEntity) {
        this.id = pBuyerAddressEntity.getId();
        this.uid = pBuyerAddressEntity.getUid();
        this.name = pBuyerAddressEntity.getName();
        this.mobile = pBuyerAddressEntity.getMobile();
        this.province = pBuyerAddressEntity.getProvince();
        this.city = pBuyerAddressEntity.getCity();
        this.district = pBuyerAddressEntity.getDistrict();
        this.detail = pBuyerAddressEntity.getDetail();
        this.isDefault = pBuyerAddressEntity.getIsDefault();
    }
}
