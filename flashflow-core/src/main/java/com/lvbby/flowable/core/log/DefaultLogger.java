package com.lvbby.flowable.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dushang.lp
 * @version $Id: DefaultLogger.java, v 0.1 2020年03月13日 上午12:47 dushang.lp Exp $
 */
public class DefaultLogger implements IFlowLogger {

    private static Logger logger = LoggerFactory.getLogger(DefaultLogger.class);

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(Exception e, String message) {
        logger.error(message, e);
    }
}