
package com.lvbby.flowable.spring;

import com.lvbby.flowable.spring.anno.EnableFlow;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * 使flow在spring中自动生效
 * @author dushang.lp
 * @version $Id: FlowSpringAutoConfiguration.java, v 0.1 2020年03月07日 19:56 dushang.lp Exp $
 */
public class FlowSpringEnableSelector extends AdviceModeImportSelector<EnableFlow> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        return new String[]{FlowSpringConfiguration.class.getName()};
    }
}