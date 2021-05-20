/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.module.impl;

import com.lvbby.flashflow.core.model.FLowScriptConfig;
import com.lvbby.flashflow.core.model.FlowContext;
import com.lvbby.flashflow.core.module.AbstractFlowModule;
import com.lvbby.flashflow.core.module.impl.ForeachModule.ForeachModuleConfig;
import com.lvbby.flashflow.core.util.FlowCommonUtils;

import java.util.Collection;

/**
 *
 * @author dushang.lp
 * @version : ForeachModule.java, v 0.1 2021年05月19日 9:44 下午 dushang.lp Exp $
 */
public class ForeachModule extends AbstractFlowModule<FlowContext, ForeachModuleConfig> {
    @Override
    public void exec(FlowContext context) throws Exception {
        ForeachModuleConfig nodeProps = getNodeProps(context);
        Object src = null;
        //1. from context
        if (FlowCommonUtils.isNotBlank(nodeProps.fromContextKey)) {
            src = context.get(nodeProps.fromContextKey);
        }
        //2. from script
        if (src == null) {
            src = nodeProps.exec(context);
        }
        if (src == null) {
            return;
        }
        FlowCommonUtils.isTrue(src instanceof Collection, "foreach content must collection type");
        Collection dataList = (Collection) src;
        if (!FlowCommonUtils.isNotEmpty(dataList)) {
            return;
        }

        for (Object item : dataList) {
            context.put(nodeProps.itemKey, item);

        }

    }

    public static class ForeachModuleConfig extends FLowScriptConfig {
        String itemKey = "item";

        boolean catchException = false;

        boolean catchSignal = true;

        String fromContextKey;
    }
}