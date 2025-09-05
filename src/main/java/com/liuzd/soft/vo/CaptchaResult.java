package com.liuzd.soft.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaResult {
    /**
     * 验证码图片的Base64编码
     */
    private String imageBase64;

    /**
     * 验证码文本
     */
    private String captchaCode;
}
