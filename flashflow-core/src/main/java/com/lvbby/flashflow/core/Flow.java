
package com.lvbby.flashflow.core;

import com.google.common.collect.Lists;
import com.lvbby.flashflow.core.anno.FlowProp;
import com.lvbby.flashflow.core.anno.FlowPropConfig;
import com.lvbby.flashflow.core.config.FlowConfig;
import com.lvbby.flashflow.core.config.FlowConfigParser;
import com.lvbby.flashflow.core.error.FlowException;
import com.lvbby.flashflow.core.model.FlowActionInfo;
import com.lvbby.flashflow.core.model.FlowPropInfo;
import com.lvbby.flashflow.core.model.FlowStreamModel;
import com.lvbby.flashflow.core.utils.FlowHelper;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowEngine.java, v 0.1 2020年03月06日 下午5:16 dushang.lp Exp $
 */
public class Flow {

    static {
        /** load actions */
        ServiceLoader<IFlowAction> load = ServiceLoader.load(IFlowAction.class);
        load.forEach(iFlowAction -> FlowContainer.registerFlowAction(iFlowAction));
    }
    public static void loadConfig(String jsonConfig) {
        FlowConfig config = FlowConfigParser.parse(jsonConfig);
        /** 1. script */
        if (FlowUtils.isNotEmpty(config.getScripts())) {
            config.getScripts().forEach(s -> FlowContainer.addFlowConfig(s));
        }
        /** 2. props */
        if (config.getProps() != null) {
            FlowContainer.getGlobalProps().putAll(config.getProps());
        }

    }

    /***
     * 扫描属性,用来做属性解析
     * @see FlowProp
     * @see FlowPropConfig
     * @param packageName
     * @throws Exception
     */
    public static void scanProps(String... packageName) throws Exception {
        scanActionProps(packageName);
        scanGlobalProps(packageName);
    }

    /***
     * local模式自动注册action
     * @param packages
     */
    public static void scanActions(String... packages) {
        List<Class> actionClasses = FlowUtils.scan(c ->
                        !Modifier.isAbstract(c.getModifiers())
                        && !Modifier.isInterface(c.getModifiers())
                        && FlowUtils.isClassOf(c, IFlowAction.class)
                , packages);
        for (Class clz : actionClasses) {
            IFlowAction flowAction = (IFlowAction) FlowUtils.newInstance(clz);
            FlowContainer.registerFlowAction(flowAction);
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
        FlowUtils.isTrue(context.getConfig() != null, String.format("config not found for code:%s", context.getCode()));
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FlowContext.cleanContext();
        }
    }

    /***
     * 1. 终止流程：设置stopFlag或抛异常
     * 2. 跳过某个action，${actionId}#_skip,通过属性跳过
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

    /**
     * 扫描所有全局props
     * @param packages
     * @return
     * @throws Exception
     */
    private static void scanGlobalProps(String... packages) {
        List<Class> props = FlowUtils.scan(c ->
                        !Modifier.isAbstract(c.getModifiers())
                        && !Modifier.isInterface(c.getModifiers())
                        && c.isAnnotationPresent(FlowPropConfig.class)
                , packages);
        props.forEach(c -> {
            List<FlowPropInfo> flowPropInfos = buildProps(c);
            for (FlowPropInfo flowPropInfo : flowPropInfos) {
                FlowUtils.isTrue(!FlowContainer.propMetaInfo.containsKey(flowPropInfo.getKey()),
                        String.format("duplicated key[%s],info:%s", flowPropInfo.getKey(), flowPropInfo));
                FlowContainer.propMetaInfo.put(flowPropInfo.getKey(), flowPropInfo);
            }
        });
    }

    /***
     * 扫描所有的action的prop
     * @param packages
     * @return
     * @throws Exception
     */
    private static void scanActionProps(String... packages) {
        List<Class> classes = _scanActions(packages);
        FlowContainer.actionMetaInfo = classes.stream().map(clz -> {
            FlowActionInfo flowActionInfo = new FlowActionInfo();
            flowActionInfo.actionClass = clz;
            flowActionInfo.name = ((IFlowAction) FlowUtils.newInstance(clz)).actionId();
            flowActionInfo.props = buildProps(clz);
            return flowActionInfo;
        }).collect(Collectors.toList());
    }

    private static List<FlowPropInfo> buildProps(Class<?> actionClass) {
        try {
            boolean isGlobal = actionClass.isAnnotationPresent(FlowPropConfig.class);
            List<FlowPropInfo> re = Lists.newLinkedList();
            for (Field f : actionClass.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.isAnnotationPresent(FlowProp.class)) {
                    FlowProp annotation = f.getAnnotation(FlowProp.class);
                    f.setAccessible(true);
                    FlowPropInfo flowPropInfo = new FlowPropInfo();
                    flowPropInfo.setActionClass(actionClass);
                    flowPropInfo.setDesc(annotation.value());
                    flowPropInfo.setGlobal(isGlobal);
                    Class<?> type = f.getType();
                    if (FlowUtils.isClassOf(type, FlowKey.class)) {
                        Type paramType = ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
                        FlowKey o = (FlowKey) f.get(null);
                        flowPropInfo.setKey(o.getKey());
                        //支持泛型参数
                        flowPropInfo.setType(paramType);
                    }
                    if (FlowUtils.isClassOf(type, String.class)) {
                        flowPropInfo.setKey((String) f.get(null));
                    }
                    re.add(flowPropInfo);
                }
            }
            return re;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 扫描action
     * @param packages
     * @return
     * @throws Exception
     */
    private static List<Class> _scanActions(String... packages) {
        List<Class> actionClasses = FlowUtils.scan(c ->
                        !Modifier.isAbstract(c.getModifiers())
                        && !Modifier.isInterface(c.getModifiers())
                        && FlowUtils.isClassOf(c, IFlowAction.class)
                , packages);
        return actionClasses;
    }
}