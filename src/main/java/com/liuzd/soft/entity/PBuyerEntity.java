package com.liuzd.soft.entity;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liuzd.soft.consts.GlobalConstant;
import lombok.Data;

/**
 * 商城用户表
 */
@Data
@DS(value = GlobalConstant.DEFAULT_DB_KEY)
@TableName("buyer")
public class PBuyerEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 密码
     */
    private String password;

    /**
     * 加密salt
     */
    private String salt;
}
