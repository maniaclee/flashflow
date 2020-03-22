
package com.lvbby.flashflow.core.utils;

import com.lvbby.flashflow.core.FlowContext;
import com.lvbby.flashflow.core.FlowFrameWorkKeys;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.IFlowActionExtension;
import com.lvbby.flashflow.core.anno.FlowAction;
import com.lvbby.flashflow.core.anno.FlowExt;
import com.lvbby.flashflow.core.error.FlowErrorCodeEnum;
import com.lvbby.flashflow.core.error.FlowException;
import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Optional;

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

    public static boolean isClassOf(Class toCheck, Class target) {
        return ClassUtils.isAssignable(toCheck, target);
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
    public static Object getProp(String key) {
        FlowContext flowContext = FlowContext.currentContext();
        return flowContext.getConfig().getExtProperty(flowContext, key);
    }

    public static String getFlowActionName(IFlowAction action) {
        String actionName = action.actionName();
        if (FlowUtils.isBlank(actionName)) {
            FlowAction annotation = getAnnotation(action.getClass(), FlowAction.class);
            if (annotation != null) {
                actionName = annotation.id();
            }
        }
        return actionName;
    }

    public static String getFlowExtName(Class extensionClz) {
        FlowExt annotation = getAnnotation(extensionClz, FlowExt.class);
        return Optional.ofNullable(annotation).map(FlowExt::value).orElse(null);
    }

    public static void isTrue(boolean expr, String msg) {
        if (!expr) {
            throw new FlowException(FlowErrorCodeEnum.systemError, msg);
        }
    }

    public static <T> T newInstance(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("failed to instance %s", clz.getName()), e);
        }
    }

    public static <A extends Annotation> A getAnnotation(Class clz, Class<A> annotation) {
        A re = (A) clz.getAnnotation(annotation);
        if (re != null) { return re; }
        re = ClassUtils.getAllInterfaces(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
                .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        if (re == null) {
            re = ClassUtils.getAllSuperclasses(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
                    .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        }
        return re;
    }

}