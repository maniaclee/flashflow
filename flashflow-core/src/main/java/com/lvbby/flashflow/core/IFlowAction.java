
package com.lvbby.flashflow.core;

import com.lvbby.flashflow.core.anno.FlowAction;
import com.lvbby.flashflow.core.utils.FlowUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowAction.java, v 0.1 2020年03月06日 下午5:14 dushang.lp Exp $
 */
public interface IFlowAction<IContext extends FlowContext> {
    void invoke(IContext context) throws Exception;

    /***
     * 1. @FlowAction
     * 2.class.name
     * @return
     */
    default String actionName() {
        FlowAction annotation = FlowUtils.getAnnotation(getClass(), FlowAction.class);
        String id = getClass().getName();
        if (annotation != null) {
            id = annotation.id();
        }
        if(StringUtils.isBlank(id)){
            id = StringUtils.uncapitalize(getClass().getSimpleName());
        }
        return id;
    }

}