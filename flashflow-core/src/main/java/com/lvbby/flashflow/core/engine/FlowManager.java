/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.engine;

import com.lvbby.flashflow.core.model.FlowConfig;
import com.lvbby.flashflow.core.module.FlowModule;

/**
 *
 * @author dushang.lp
 * @version : IFlowManager.java, v 0.1 2021年05月16日 下午2:48 dushang.lp Exp $
 */
public interface IFlowManager {
    FlowConfig getFlowConfig(String flowId);

    FlowModule getModule(String moduleId);

    void registerConfig(FlowConfig flowConfig);

    void registerModule(FlowModule module);
}