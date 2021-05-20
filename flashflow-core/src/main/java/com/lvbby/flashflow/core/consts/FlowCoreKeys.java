/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core;

import com.lvbby.flashflow.core.model.FlowKey;
import com.lvbby.flashflow.core.model.Node;

/**
 *
 * @author dushang.lp
 * @version : FlowCoreKeys.java, v 0.1 2021年05月16日 下午4:09 dushang.lp Exp $
 */
public class FlowCoreKeys {

    public static FlowKey<Node> currentNode = new FlowKey<>("_currentNode");

    public static FlowKey<String> scriptId = new FlowKey<>("_scriptId");

}