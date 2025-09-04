package com.liuzd.soft.vo.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
@Data
@JsonSerialize
public class EditAddressReq {

    private Integer addressId;

    @NotBlank
    private String receiverName;
    @NotBlank
    private String receiverPhone;
    @NotNull
    private Integer provinceId;
    @NotNull
    private Integer cityId;
    @NotNull
    private Integer districtId;
    @NotBlank
    private String detailAddress;
    private Boolean isDefault;
}
