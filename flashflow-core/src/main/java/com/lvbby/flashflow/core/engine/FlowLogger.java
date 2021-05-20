/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core;

/**
 *
 * @author dushang.lp
 * @version : FlowLogger.java, v 0.1 2021年05月16日 下午4:17 dushang.lp Exp $
 */
public interface FlowLogger {

    void info(String message);

    void error(Throwable e, String message);
}