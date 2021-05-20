/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.module;

import com.lvbby.flashflow.core.model.FlowContext;
import com.lvbby.flashflow.core.util.FlowUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * @author dushang.lp
 * @version : AbstractFlowModule.java, v 0.1 2021年05月19日 9:46 下午 dushang.lp Exp $
 */
public abstract class AbstractFlowModule<T extends FlowContext,C> implements FlowModule<T> {

    private Class<C> configType;

    {
        Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        this.configType = (Class<C>) type;
    }

    /**
     * get current config data
     * @param ctx
     * @return
     */
    public C getNodeProps(T ctx) {
        return FlowUtils.getNodeProps(ctx, configType);
    }
}