package com.lvbby.flowable.core;

/**
 *
 * @author dushang.lp
 * @version $Id: IFlowkey.java, v 0.1 2020年03月08日 15:13 dushang.lp Exp $
 */
public class FlowKey<T> {

    private String key;

    public FlowKey(String key) {
        this.key = key;
    }

    /**
     * Getter method for property   key.
     *
     * @return property value of key
     */
    public String getKey() {
        return key;
    }
}