/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.ex;

/**
 *
 * @author dushang.lp
 * @version : FlowException.java, v 0.1 2021年05月19日 9:22 下午 dushang.lp Exp $
 */
public class FlowException extends RuntimeException{

    private FlowExceptionTypeEnum type;

    public FlowException(FlowExceptionTypeEnum type) {
        super("flowStop");
        this.type = type;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public FlowExceptionTypeEnum getType() {
        return type;
    }
}