package com.lvbby.flashflow.core.error;

/**
 * flow异常
 * @author dushang.lp
 * @version $Id: FlowException.java, v 0.1 2020年03月13日 上午12:12 dushang.lp Exp $
 */
public class FlowException extends RuntimeException {
    FlowErrorCodeEnum code;

    public FlowException(FlowErrorCodeEnum errorCodeEnum, String message) {
        super(String.format("flow-error[%s]:%s", errorCodeEnum.name(), message));
        this.code = errorCodeEnum;
    }

    /**
     * Getter method for property   code.
     *
     * @return property value of code
     */
    public FlowErrorCodeEnum getCode() {
        return code;
    }
}