package com.liuzd.soft.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.liuzd.soft.entity.PBuyerAddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
public class AddressResp {

    @JsonProperty("addressId")
    private Integer addressId;

    @JsonProperty("receiverName")
    private String receiverName;
    @JsonProperty("receiverPhone")
    private String receiverPhone;
    @JsonProperty
    private Integer provinceId;
    @JsonProperty
    private Integer cityId;
    @JsonProperty
    private Integer districtId;
    @JsonProperty("detailAddress")
    private String detailAddress;
    private Boolean isDefault;

    public AddressResp(PBuyerAddressEntity pBuyerAddressEntity) {
        this.addressId = pBuyerAddressEntity.getId();
        this.receiverName = pBuyerAddressEntity.getName();
        this.receiverPhone = pBuyerAddressEntity.getMobile();
        this.provinceId = pBuyerAddressEntity.getProvince();
        this.cityId = pBuyerAddressEntity.getCity();
        this.districtId = pBuyerAddressEntity.getDistrict();
        this.detailAddress = pBuyerAddressEntity.getDetail();
        this.isDefault = pBuyerAddressEntity.getIsDefault() == 1;
    }
}
