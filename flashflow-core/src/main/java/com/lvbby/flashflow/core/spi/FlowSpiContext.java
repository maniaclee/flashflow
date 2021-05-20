/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.spi;

import com.lvbby.flashflow.core.model.Node;

/**
 *
 * @author dushang.lp
 * @version : FlowNodeSpiContext.java, v 0.1 2021年05月16日 下午9:52 dushang.lp Exp $
 */
public class FlowNodeSpiContext {
    private Exception exception;
    private long      startTime;
    private long      endTime;
    private Node      node;

    /**
     * Getter method for property <tt>startTime</tt>.
     *
     * @return property value of startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Setter method for property <tt>startTime</tt>.
     *
     * @param startTime  value to be assigned to property startTime
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter method for property <tt>endTime</tt>.
     *
     * @return property value of endTime
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Setter method for property <tt>endTime</tt>.
     *
     * @param endTime  value to be assigned to property endTime
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    /**
     * Getter method for property <tt>node</tt>.
     *
     * @return property value of node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Setter method for property <tt>node</tt>.
     *
     * @param node  value to be assigned to property node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Getter method for property <tt>exception</tt>.
     *
     * @return property value of exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Setter method for property <tt>exception</tt>.
     *
     * @param exception  value to be assigned to property exception
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }
}