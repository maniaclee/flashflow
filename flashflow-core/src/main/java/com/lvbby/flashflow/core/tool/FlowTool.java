package com.lvbby.flashflow.core.tool;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lvbby.flashflow.core.FlowKey;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.anno.FlowGlobalProp;
import com.lvbby.flashflow.core.anno.FlowProp;
import com.lvbby.flashflow.core.template.BeetlTemplateEngine;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * flow 常用工具
 * @author dushang.lp
 * @version $Id: FlowTool.java, v 0.1 2020年03月29日 下午1:45 dushang.lp Exp $
 */
public class FlowTool {

    /***
     * 生成flow的文档
     * @param packageName
     * @return
     */
    public static String createFlowDoc(String packageName) {
        FlowOutlineInfo flowOutlineInfo = new FlowOutlineInfo();
        flowOutlineInfo.globalProps = scanGlobalProps(packageName);
        flowOutlineInfo.actions = scanActionProps(packageName);
        System.out.println(JSON.toJSONString(flowOutlineInfo,true));

        return new BeetlTemplateEngine(FlowUtils.readResourceFile("templates/README.md")).bind("src", flowOutlineInfo).render();
    }

    /***
     * 扫描action
     * @param packages
     * @return
     * @throws Exception
     */
    public static List<Class> scanActions(String... packages) {
        return FlowUtils.scan(c ->
                        !Modifier.isAbstract(c.getModifiers())
                        && !Modifier.isInterface(c.getModifiers())
                        && FlowUtils.isClassOf(c, IFlowAction.class)
                , packages);
    }

    /***
     * 扫描所有全局props
     * @param packages
     * @return
     * @throws Exception
     */
    public static List<FlowPropInfo> scanGlobalProps(String packages) {
        List<FlowPropInfo> re = Lists.newLinkedList();
        List<Class> props = FlowUtils.scan(c ->
                        !Modifier.isAbstract(c.getModifiers())
                        && !Modifier.isInterface(c.getModifiers())
                        && c.isAnnotationPresent(FlowGlobalProp.class)
                , packages);
        props.forEach(c -> re.addAll(buildProps(c)));
        return re;
    }

    /***
     * 扫描所有的action的prop
     * @param packages
     * @return
     * @throws Exception
     */
    public static List<FlowActionInfo> scanActionProps(String packages) {
        List<Class> classes = scanActions(packages);
        return classes.stream().map(clz -> {
            FlowActionInfo flowActionInfo = new FlowActionInfo();
            flowActionInfo.actionClass = clz;
            flowActionInfo.name = ((IFlowAction) FlowUtils.newInstance(clz)).actionName();
            flowActionInfo.props = buildProps(clz);
            return flowActionInfo;
        }).collect(Collectors.toList());
    }

    private static List<FlowPropInfo> buildProps(Class<?> actionClass) {
        try {
            boolean isGlobal = actionClass.isAnnotationPresent(FlowGlobalProp.class);
            List<FlowPropInfo> re = Lists.newLinkedList();
            for (Field f : actionClass.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.isAnnotationPresent(FlowProp.class)) {
                    FlowProp annotation = f.getAnnotation(FlowProp.class);
                    f.setAccessible(true);
                    FlowPropInfo flowPropInfo = new FlowPropInfo();
                    flowPropInfo.setActionClass(actionClass);
                    flowPropInfo.setDesc(annotation.desc());
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

}