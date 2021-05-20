/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.engine;

import com.lvbby.flashflow.core.model.FlowConfig;
import com.lvbby.flashflow.core.model.FlowContext;
import com.lvbby.flashflow.core.model.Node;

/**
 *
 * @author dushang.lp
 * @version : DefaultFlowExecutor.java, v 0.1 2021年05月14日 下午10:33 dushang.lp Exp $
 */
public class DefaultFlowExecutor<T extends  FlowContext> implements IFlowExecutor<T>{

    @Override
    public void exec(T flowContext) {
        FlowConfig config = flowContext.getConfig();
        Node flow = config.getFlow();

    }

}