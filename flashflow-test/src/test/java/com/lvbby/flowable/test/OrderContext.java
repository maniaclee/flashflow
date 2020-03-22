package com.lvbby.flowable.test;

import com.lvbby.flowable.core.FlowContext;

/**
 *
 * @author dushang.lp
 * @version $Id: OrderContext.java, v 0.1 2020年03月08日 14:42 dushang.lp Exp $
 */
public class OrderContext extends FlowContext {
    private String orderId;

    public OrderContext(String code) {
        super(code);
    }

    public OrderContext() {
    }

    /**
     * Getter method for property   orderId.
     *
     * @return property value of orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Setter method for property   orderId .
     *
     * @param orderId  value to be assigned to property orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}