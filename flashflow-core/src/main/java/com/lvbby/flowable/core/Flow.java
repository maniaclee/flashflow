
package com.lvbby.flowable.core;

import com.lvbby.flowable.core.error.FlowException;
import com.lvbby.flowable.core.utils.FlowHelper;
import com.lvbby.flowable.core.utils.FlowUtils;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowEngine.java, v 0.1 2020年03月06日 下午5:16 dushang.lp Exp $
 */
public class Flow {

    public static <IContext extends FlowContext> void execSimple(IContext context, IFlowAction... actions) {
        FlowNode flowNode = FlowNode.ofArray(actions);
        context.setConfig(FlowScript.anonymous(flowNode));
        exec(context);
    }

    public static <IContext extends FlowContext> void exec(IContext context) {
        /** 初始化上下文 */
        if (context.getConfig() == null) {
            context.setConfig(FlowContainer.getFlowConfig(context.getCode()));
        }
        FlowContext.buildCurrentContext(context);

        try {
            FlowNode node = context.getConfig().getPipeline();
            /** 标记当前的节点 */
            context.put(FlowFrameWorkKeys.currentNode, node);
            _doExec(context, node);
        } catch (FlowException e) {
            switch (e.getCode()) {
                case terminateFlow:
                    return;
                default:
                    throw e;
            }
        } finally {
            FlowContext.cleanContext();
        }
    }

    /***
     * 1. 终止流程：设置stopFlag或抛异常
     * 2. 跳过某个action，${actionName}#_skip,通过属性跳过
     * @param context
     * @param node
     * @param <IContext>
     */
    private static <IContext extends FlowContext> void _doExec(IContext context, FlowNode node) {
        Boolean stopFlag = context.get(FlowFrameWorkKeys.stopFlag);
        /** stop flow */
        if (stopFlag != null && stopFlag) {
            return;
        }
        if (node.getCondition() == null || node.getCondition().test(context)) {
            String actionId = node.getActionId();
            IFlowAction flowAction = FlowContainer.getFlowAction(actionId);
            if (flowAction == null) {
                throw new RuntimeException("flowAction unknown: " + actionId);
            }
            /** action can be skipped by customized props */
            Object ignore = FlowHelper.getProp("_skip");
            if (!(ignore != null && ignore instanceof Boolean && (Boolean) ignore)) {
                flowAction.invoke(context);
            }
            List<FlowNode> children = node.getChildren();
            /** invoke children */
            if (FlowUtils.isNotEmpty(children)) {
                for (FlowNode child : children) {
                    _doExec(context, child);
                }
            }
        }
    }

}