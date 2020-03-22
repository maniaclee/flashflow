package com.lvbby.flowable.core.log;

/**
 *
 * @author dushang.lp
 * @version $Id: VoidFlowLogger.java, v 0.1 2020年03月13日 上午9:26 dushang.lp Exp $
 */
public class VoidFlowLogger implements IFlowLogger{
    @Override
    public void info(String message) {

    }

    @Override
    public void debug(String message) {

    }

    @Override
    public void warn(String message) {

    }

    @Override
    public void error(Exception e, String message) {

    }
}