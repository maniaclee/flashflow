/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.engine;

import com.google.common.collect.Maps;
import com.lvbby.flashflow.core.engine.impl.DefaultFlowExecutor;
import com.lvbby.flashflow.core.engine.impl.DefaultFlowManager;
import com.lvbby.flashflow.core.model.FlowConfig;
import com.lvbby.flashflow.core.model.FlowContext;
import com.lvbby.flashflow.core.script.FlowScriptEngine;
import com.lvbby.flashflow.core.script.impl.GroovyScriptEngine;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version : FlowEngine.java, v 0.1 2021年05月14日 下午10:24 dushang.lp Exp $
 */
public class FlowEngine<T extends FlowContext> {

    /**
     * flow manager
     */
    private FlowManager flowManager = new DefaultFlowManager();

    /**
     * flow executor
     */
    private FlowExecutor<T> flowExecutor = new DefaultFlowExecutor<>();


    /**
     * engine to execute all kinds of script , like springExp,groovy etc.
     */
    private Map<String, FlowScriptEngine> scriptEngine = Maps.newHashMap();

    public void init(){
        scriptEngine.put("groovy", new GroovyScriptEngine());
    }

    public void start(T ctx) throws Exception {
        String flowId = ctx.getFlowId();
        FlowConfig flowConfig = flowManager.getFlowConfig(flowId);
        ctx.setConfig(flowConfig);
        flowExecutor.exec(ctx);
    }

    /**
     * Getter method for property <tt>expressionParsers</tt>.
     *
     * @return property value of expressionParsers
     */
    public Map<String, FlowScriptEngine> getScriptEngine() {
        return scriptEngine;
    }

    /**
     * Setter method for property <tt>expressionParsers</tt>.
     *
     * @param scriptEngine  value to be assigned to property expressionParsers
     */
    public void setScriptEngine(Map<String, FlowScriptEngine> scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    private T createCtx(){
        return null;
    }

    /**
     * Getter method for property <tt>flowManager</tt>.
     *
     * @return property value of flowManager
     */
    public FlowManager getFlowManager() {
        return flowManager;
    }

    /**
     * Setter method for property <tt>flowManager</tt>.
     *
     * @param flowManager  value to be assigned to property flowManager
     */
    public void setFlowManager(FlowManager flowManager) {
        this.flowManager = flowManager;
    }

    /**
     * Getter method for property <tt>flowExecutor</tt>.
     *
     * @return property value of flowExecutor
     */
    public FlowExecutor<T> getFlowExecutor() {
        return flowExecutor;
    }

    /**
     * Setter method for property <tt>flowExecutor</tt>.
     *
     * @param flowExecutor  value to be assigned to property flowExecutor
     */
    public void setFlowExecutor(FlowExecutor<T> flowExecutor) {
        this.flowExecutor = flowExecutor;
    }
}