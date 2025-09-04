package com.liuzd.soft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商城用户DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PBuyerDto {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * uuid
     */
    private String uuid;
    private String uname;
    private String mobile;

    /**
     * 性别 1男，2女，0未知
     */
    private Integer sex;

    /**
     * 生日：2008-08-08
     */
    private String birth;

    /**
     * 创建日期
     */
    private String createTime;

    /**
     * 等级经验值
     */
    private Integer levelValue;
}
