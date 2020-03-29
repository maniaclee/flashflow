
package com.lvbby.flashflow.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.lvbby.flashflow.core.error.FlowException;
import com.lvbby.flashflow.core.model.FlowStreamModel;
import com.lvbby.flashflow.core.utils.FlowHelper;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowEngine.java, v 0.1 2020年03月06日 下午5:16 dushang.lp Exp $
 */
public class Flow {

    /***
     * scanAction
     * @param packageName
     * @throws Exception
     */
    public static void scan(String packageName) throws Exception {
        ImmutableSet<ClassInfo> cls = ClassPath.from(Flow.class.getClassLoader()).getTopLevelClassesRecursive(
                packageName);
        for (ClassInfo cl : cls) {
            Class<?> clz = cl.load();
            if (FlowUtils.isClassOf(clz, IFlowAction.class)  ) {
                IFlowAction flowAction = (IFlowAction) FlowUtils.newInstance(clz);
                FlowContainer.registerFlowAction(flowAction);
            }
        }
    }

    /***
     * 便捷执行actions，无config
     * @param context
     * @param actions
     * @param <IContext>
     */
    public static <IContext extends FlowContext> void execSimple(IContext context, IFlowAction... actions) {
        FlowNode flowNode = FlowNode.ofArray(actions);
        context.setConfig(FlowScript.of(flowNode));
        exec(context);
    }

    /***
     * 执行一个临时流程FlowNode，无config
     * @param context
     * @param node
     * @param <IContext>
     */
    public static <IContext extends FlowContext> void exec(IContext context, FlowNode node) {
        context.setConfig(FlowScript.of(node));
        exec(context);
    }

    /***
     * 通用的执行流程
     * @param context
     * @param <IContext>
     */
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
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        finally {
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
    private static <IContext extends FlowContext> void _doExec(IContext context, FlowNode node) throws Exception {
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

                /** dispatch stream */
                FlowStreamModel flowStreamModel = context.get(FlowFrameWorkKeys.stream);
                if (flowStreamModel != null && flowStreamModel.targetlist != null) {
                    for (Object targetObject : flowStreamModel.targetlist) {
                        context.putValue(flowStreamModel.key, targetObject);
                        _invokeChildren(context, node);
                    }
                    return;
                }
            }
            _invokeChildren(context, node);
        }
    }

    private static <IContext extends FlowContext> void _invokeChildren(IContext context, FlowNode node) throws Exception {
        List<FlowNode> children = node.getChildren();
        /** invoke children */
        if (FlowUtils.isNotEmpty(children)) {
            for (FlowNode child : children) {
                _doExec(context, child);
            }
        }
    }

}