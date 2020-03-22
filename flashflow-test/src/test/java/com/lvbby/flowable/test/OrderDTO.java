
package com.lvbby.flowable.test;

/**
 *
 * @author dushang.lp
 * @version $Id: OrderDTO.java, v 0.1 2020年03月07日 15:35 dushang.lp Exp $
 */
public class OrderDTO {
    private String orderId;
    private String title;
    private String status;

    /**
     * Getter method for property   status.
     *
     * @return property value of status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter method for property   status .
     *
     * @param status  value to be assigned to property status
     */
    public void setStatus(String status) {
        this.status = status;
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

    /**
     * Getter method for property   title.
     *
     * @return property value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter method for property   title .
     *
     * @param title  value to be assigned to property title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}