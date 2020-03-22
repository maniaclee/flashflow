
package com.lvbby.flowable.spring.anno;

import com.lvbby.flowable.spring.FlowSpringEnableSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author dushang.lp
 * @version $Id: EnableFlow.java, v 0.1 2020年03月07日 19:55 dushang.lp Exp $
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FlowSpringEnableSelector.class)
public @interface EnableFlow {

    AdviceMode mode() default AdviceMode.PROXY;
}