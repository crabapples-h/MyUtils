package com.MissX.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 使用空验证工具时在需要验证的参数上加此注解
 * 2019年1月8日 下午3:02:18
 * @author H
 * TODO
 * Admin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MissX {

}
