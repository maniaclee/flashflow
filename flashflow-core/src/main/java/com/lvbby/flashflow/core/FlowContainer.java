
package com.lvbby.flashflow.core;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lvbby.flashflow.core.model.FlowActionInfo;
import com.lvbby.flashflow.core.model.FlowPropInfo;
import com.lvbby.flashflow.core.utils.FlowHelper;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 容器，装在全局的流程和模块
 * @author dushang.lp
 * @version $Id: FlowConfigsHolder.java, v 0.1 2020年03月06日 下午6:03 dushang.lp Exp $
 */
public class FlowContainer {
    /***
     * 全局的场景
     */
    private static Map<String, FlowScript> configMap = Maps.newHashMap();

    /***
     * 全局action
     */
    private static Map<String, IFlowAction> actionMap = Maps.newHashMap();

    /***
     * 全局属性
     */
    public static JSONObject props = new JSONObject();

    /***
     * action信息，包含name、属性meta等
     */
    public static List<FlowActionInfo> actionMetaInfo = Lists.newLinkedList();

    /**
     * 属性描述信息，包括全局+action
     * @see Flow#scanProps(java.lang.String...)
     */
    public static Map<String, FlowPropInfo> propMetaInfo = Maps.newHashMap();

    public static FlowScript getFlowConfig(String code) {
        return configMap.get(code);
    }

    public static IFlowAction getFlowAction(String actionId) {
        return actionMap.get(actionId);
    }

    /***
     * 访问所有属性
     * 1. node
     * 2. script
     * 3. 全局
     * @param consumer
     */
    public static void visitAllProp(BiConsumer<String, Map> consumer) {
        //1. 全局属性
        for (String s : props.keySet()) {
            consumer.accept(s, props);
        }
        for (FlowScript flowScript : configMap.values()) {
            Map<String, Object> props = flowScript.getProps();
            //2. script属性
            for (String s : props.keySet()) {
                consumer.accept(s,props);
            }
            //3. node属性
            flowScript.getPipeline().visit(node -> {
                JSONObject ps = node.getProps();
                if(ps!=null){
                    for (String s : ps.keySet()) {
                        consumer.accept(s,ps);
                    }
                }
            });
        }

    }
    /***
     * 注册Action
     * @param action
     */
    public static void registerFlowAction(IFlowAction action) {
        String flowActionName = FlowHelper.getFlowActionName(action);
        if (FlowUtils.isNotBlank(flowActionName)) {
            registerFlowAction(flowActionName, action);
        }
    }

    /***
     * 注册Action
     * @param actionId
     * @param action
     */
    public static void registerFlowAction(String actionId, IFlowAction action) {
        if (!actionMap.containsKey(actionId)) {
            actionMap.put(actionId, action);
        }
    }

    public static void addFlowConfig(FlowScript config) {
        configMap.put(config.getCode(), config);
    }

    /**
     * Setter method for property   configMap .
     *
     * @param configMap  value to be assigned to property configMap
     */
    public static void setConfigMap(Map<String, FlowScript> configMap) {
        FlowContainer.configMap = configMap;
    }

    /**
     * Getter method for property   configMap.
     *
     * @return property value of configMap
     */
    public static Map<String, FlowScript> getConfigMap() {
        return configMap;
    }

    /**
     * Getter method for property   actionMap.
     *
     * @return property value of actionMap
     */
    public static Map<String, IFlowAction> getActionMap() {
        return actionMap;
    }

    /**
     * Setter method for property   actionMap .
     *
     * @param actionMap  value to be assigned to property actionMap
     */
    public static void setActionMap(Map<String, IFlowAction> actionMap) {
        FlowContainer.actionMap = actionMap;
    }

    /**
     * Getter method for property   props.
     *
     * @return property value of props
     */
    public static JSONObject getProps() {
        return props;
    }
}