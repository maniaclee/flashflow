package com.lvbby.flowable.core.anno;

import com.lvbby.flowable.core.IFlowActionExtension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowExtPoint.java, v 0.1 2020年03月06日 21:39 dushang.lp Exp $
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowAction {
    Class<? extends IFlowActionExtension>[] value() default {};

    String id() default "";

}