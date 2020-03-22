package com.lvbby.flowable.core;

import com.lvbby.flowable.core.utils.FlowHelper;

/**
 *
 * @author dushang.lp
 * @version $Id: AbstractFlowAction.java, v 0.1 2020年03月10日 上午11:47 dushang.lp Exp $
 */
public abstract class AbstractFlowAction<T extends FlowContext> implements IFlowAction<T> {

    /***
     * 获取扩展
     * @param extClass
     * @param <IExt>
     * @return
     */

    public <IExt extends IFlowActionExtension> IExt getExtension(Class<IExt> extClass) {
        return FlowHelper.getExtension(FlowContext.currentContext(), extClass);
    }

    public <PROP> PROP getPropOrDefault(String key, PROP defaultValue) {
        Object prop = FlowHelper.getProp(key);
        return prop == null ? defaultValue : (PROP) prop;
    }

    public void stopFlow(){
        FlowHelper.stopFlow();
    }
    public void stopFlowInterrupt(){
        FlowHelper.stopFlowInterrupt();
    }
}