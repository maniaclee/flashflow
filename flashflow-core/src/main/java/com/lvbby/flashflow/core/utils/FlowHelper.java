
package com.lvbby.flashflow.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.flashflow.core.FlowContainer;
import com.lvbby.flashflow.core.FlowContext;
import com.lvbby.flashflow.core.FlowFrameWorkKeys;
import com.lvbby.flashflow.core.FlowKey;
import com.lvbby.flashflow.core.FlowNode;
import com.lvbby.flashflow.core.FlowScript;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.IFlowActionExtension;
import com.lvbby.flashflow.core.anno.FlowAction;
import com.lvbby.flashflow.core.anno.FlowExt;
import com.lvbby.flashflow.core.config.GroovyFunction;
import com.lvbby.flashflow.core.error.FlowErrorCodeEnum;
import com.lvbby.flashflow.core.error.FlowException;
import com.lvbby.flashflow.core.model.FlowPropInfo;
import com.lvbby.flashflow.core.model.FlowStreamModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowHelper.java, v 0.1 2020年03月06日 22:09 dushang.lp Exp $
 */
public class FlowHelper {

    /***
     * 终止流程
     */
    public static void stopFlow() {
        FlowContext.currentContext().put(FlowFrameWorkKeys.stopFlag, true);
    }

    /***
     * 终止流程
     */
    public static void stopFlowInterrupt() {
        throw new FlowException(FlowErrorCodeEnum.terminateFlow, "terminateFlow");
    }

    /***
     * 获取扩展的方式
     * @param context
     * @param clz
     * @param <Ext>
     * @return
     */
    public static <Ext extends IFlowActionExtension> Ext getExtension(FlowContext context, Class<Ext> clz) {
        return context.getConfig().getExtension(context, clz);
    }

    /***
     * 获取属性的方法
     * @param key
     * @return
     */
    public static Object getPropOrDefault(String key, Object defaultValue) {
        Object prop = getProp(key);
        return prop == null ? defaultValue : prop;
    }

    public static <T> T getProp(FlowKey<T> key) {
        return (T) getProp(key.getKey());
    }

    /***
     * 分3大层：
     * 一、config层
     * @see FlowScript#getExtProperty(com.lvbby.flashflow.core.FlowContext, java.lang.String)
     * 0. node级别：    ${key}
     * 1. 多action多级：    ${alias}#${key}
     * 2. action级别：     ${actionId}#${key}
     * 3. 全局：           ${key}
     *
     * 二、全局属性
     * @see FlowContainer#getProps()
     * @param key
     * @return
     */
    public static Object getProp(String key) {
        FlowContext flowContext = FlowContext.currentContext();
        FlowNode flowNode = flowContext.currentNode();
        Object extProperty=null;
        /** 0. node上取 */
        if (flowNode.getProps() != null) {
            extProperty = flowNode.getProps().get(key);
        }
        /** 1. config加载 */
        if (extProperty == null) {
            extProperty = flowContext.getConfig().getExtProperty(flowContext, key);
        }
        /** 2. global props */
        if (extProperty == null) {
            return FlowContainer.getProps().get(key);
        }
        //兜底实时解析属性
        return parseProp(key,extProperty);
    }

    /***
     * 从value或prop里获取，顺序：value -> prop
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getValueOrProp(FlowKey<T> key) {
        return getValueOrProp(key, null);
    }

    public static <T> T getValueOrProp(FlowKey<T> key, Class<T> clz) {
        return getValueOrProp(key.getKey(), clz);
    }

    public static <T> T getValueOrProp(String key) {
        return getValueOrProp(key, null);
    }

    /***
     * 获取value或属性，分3级
     * 1. node级别
     * 2. context
     * 3. config级别属性
     * 4. 全局属性
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T getValueOrProp(String key, Class<T> clz) {
        FlowContext flowContext = FlowContext.currentContext();
        Object value =null;

        //1. context里取
        if (value == null) {
            value = flowContext.getValue(key);
        }
        //2. props里取
        if (value == null) {
            value = getProp(key);
        }
        /** 配置文件key对应的对象，通过强制指定类型来进行转化 */
        if (value instanceof JSONObject && clz != null) {
            return ((JSONObject) value).toJavaObject(clz);
        }
        FlowPropInfo flowPropInfo = FlowContainer.propMetaInfo.get(key);
        if (flowPropInfo != null) {
            Type type = flowPropInfo.getType();
            if (type instanceof ParameterizedType  && value instanceof String) {
                Class rawType = (Class) ((ParameterizedType) type).getRawType();
                //解析function
                if (FlowUtils.isClassOf(rawType, Function.class)) {
                    T t = (T) new GroovyFunction(type, value.toString());
                    return t;
                }
            }
        }
        return (T) value;
    }

    public static Object parseProp(String key , Object value){
        if (value == null || !(value instanceof String)) {
            return value;
        }
        FlowPropInfo flowPropInfo = FlowContainer.propMetaInfo.get(key);
        //function类型key解析
        if (flowPropInfo != null && flowPropInfo.getType() instanceof ParameterizedType) {
            Type type = flowPropInfo.getType();
            Class rawType = (Class) ((ParameterizedType) type).getRawType();
            //解析function并进行替换
            if (FlowUtils.isClassOf(rawType, Function.class)) {
                return new GroovyFunction(type, value.toString());
            }
        }
        return value;
    }

    /***
     * 将多个任务分发成多个批次执行
     * @param singleTaskKey
     * @param batchTask
     */
    public static void stream(String singleTaskKey, List batchTask) {
        FlowContext.currentContext().put(FlowFrameWorkKeys.stream, new FlowStreamModel(singleTaskKey, batchTask));
    }

    public static String getFlowActionName(IFlowAction action) {
        String actionName = action.actionId();
        if (FlowUtils.isBlank(actionName)) {
            FlowAction annotation = FlowUtils.getAnnotation(action.getClass(), FlowAction.class);
            if (annotation != null) {
                actionName = annotation.id();
            }
            if (FlowUtils.isBlank(actionName)) {
                actionName = action.getClass().getName();
            }
        }
        return actionName;
    }

    public static String getFlowExtName(Class extensionClz) {
        FlowExt annotation = FlowUtils.getAnnotation(extensionClz, FlowExt.class);
        return Optional.ofNullable(annotation).map(FlowExt::value).orElse(null);
    }

}