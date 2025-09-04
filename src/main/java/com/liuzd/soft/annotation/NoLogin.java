package com.liuzd.soft.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: liuzd
 * @date: 2025/9/4
 * @email: liuzd2025@qq.com
 * @desc
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLogin {
    boolean value() default true;
}
