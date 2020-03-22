package com.lvbby.flashflow.core.log;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowLogger.java, v 0.1 2020年03月13日 上午12:43 dushang.lp Exp $
 */
public interface IFlowLogger {

    void info(String message);

    void debug(String message);

    void warn(String message);

    void error(Exception e, String message);
}