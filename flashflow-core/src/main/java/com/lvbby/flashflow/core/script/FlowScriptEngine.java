/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.script;

import com.lvbby.flashflow.core.model.FlowContext;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version : FlowExprParser.java, v 0.1 2021年05月16日 下午3:00 dushang.lp Exp $
 */
public interface FlowExprParser {

    Object exec(FlowContext context, List<String> exprs);
}