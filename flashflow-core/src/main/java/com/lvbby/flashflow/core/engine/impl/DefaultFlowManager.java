/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.engine;

import com.google.common.collect.Maps;
import com.lvbby.flashflow.core.model.FlowConfig;
import com.lvbby.flashflow.core.module.FlowModule;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version : FlowManager.java, v 0.1 2021年05月14日 下午10:24 dushang.lp Exp $
 */
public class DefaultFlowManager implements IFlowManager {

    private Map<String,FlowConfig> flows = Maps.newConcurrentMap();

    private Map<String, FlowModule> modules = Maps.newConcurrentMap();


    @Override
    public FlowConfig  getFlowConfig(String flowId){
        return flows.get(flowId);
    }

    @Override
    public FlowModule getModule(String moduleId){
        return modules.get(moduleId);
    }

    @Override
    public void registerConfig(FlowConfig flowConfig){
        flows.put(flowConfig.getId(), flowConfig);
    }
    @Override
    public void registerModule(FlowModule module){
        modules.put(module.id(), module);
    }
}