package com.lvbby.flowable.core.log;

import java.util.ServiceLoader;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowLogUtils.java, v 0.1 2020年03月13日 上午12:49 dushang.lp Exp $
 */
public class FlowLogUtils {

    public static IFlowLogger iFlowLogger;

    static {
        /***
         * load logger
         */
        ServiceLoader<IFlowLogger> load = null;
        try {
            load = ServiceLoader.load(IFlowLogger.class);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (load != null && load.iterator().hasNext()) {
            iFlowLogger = load.iterator().next();
        } else {
            iFlowLogger = new DefaultLogger();
        }
    }

    public static void info(String message) {iFlowLogger.info(message);}

    public static void debug(String message) {iFlowLogger.debug(message);}

    public static void warn(String message) {iFlowLogger.warn(message);}

    public static void error(Exception e, String message) {iFlowLogger.error(e, message);}
}