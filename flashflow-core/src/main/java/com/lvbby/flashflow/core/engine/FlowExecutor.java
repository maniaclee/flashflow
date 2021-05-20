/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.engine;

import com.lvbby.flashflow.core.model.FlowContext;

/**
 *
 * @author dushang.lp
 * @version : IFlowExecutor.java, v 0.1 2021年05月14日 下午10:27 dushang.lp Exp $
 */
public interface IFlowExecutor<T extends FlowContext> {

    void exec(T flowContext) throws Exception;
}