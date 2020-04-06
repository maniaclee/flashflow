package com.lvbby.flashflow.core.anno;

import com.lvbby.flashflow.core.IFlowActionExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块接口
 * @author dushang.lp
 * @version $Id: FlowExtPoint.java, v 0.1 2020年03月06日 21:39 dushang.lp Exp $
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowAction {
    /***
     * action提供的扩展接口
     * @return
     */
    Class<? extends IFlowActionExtension>[] value() default {};

    /***
     * action的ID，不指定则使用默认策略生成：
     * 1. 类名的lowerCamel模式，通spring bean name策略
     * 2. spring环境下为beanName
     * @return
     */
    String id() default "";
}