/*
 * 描述：〈权限自定义注解类〉
 * 创建人：can.du
 * 创建时间：2016-4-22
 */
package com.yunip.web.common;

/**
 * 权限自定义注解类
 */
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {
    String value() default "";
}