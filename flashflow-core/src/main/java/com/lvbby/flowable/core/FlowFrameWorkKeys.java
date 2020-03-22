package com.lvbby.flowable.core;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowFrameWorkKeys.java, v 0.1 2020年03月08日 15:13 dushang.lp Exp $
 */
public class FlowFrameWorkKeys {

    /***
     * 当前执行的node
     */
    public static FlowKey<FlowNode> currentNode = new FlowKey<>("currentNode");
    /***
     * 流程是否继续
     */
    public static FlowKey<Boolean> stopFlag = new FlowKey<>("stopFlag");
}