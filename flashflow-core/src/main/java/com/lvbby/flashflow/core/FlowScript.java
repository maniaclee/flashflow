
package com.lvbby.flashflow.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lvbby.flashflow.core.utils.FlowHelper;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowConfig.java, v 0.1 2020年03月06日 下午5:10 dushang.lp Exp $
 */
public class FlowScript {
    /** 流程id */
    private String   code;
    /** 流程 */
    private FlowNode pipeline;

    /***
     * 不管ext还是pros都有几层复用问题：
     * 1. 平台prop/ext被多个action使用
     *
     * ${alias}#${actionId}${extensionKey} : value
     * 前面的可以key可以没有
     */
    private Map<String, IFlowActionExtension> extensions = Maps.newHashMap();
    private Map<String, Object>               props      = Maps.newHashMap();

    /***
     * 临时流程，不向容器注册
     * @param node
     * @return
     */
    public static FlowScript of(FlowNode node) {
        return of(null, node);
    }

    public static FlowScript of(IFlowAction... actions) {
        FlowUtils.isTrue(actions != null && actions.length > 0, "actions can't be empty");
        return of(null, FlowNode.ofArray(actions));
    }

    public static FlowScript of(String code) {
        return of(code, null);
    }

    public static FlowScript of(String code, FlowNode flowScript) {
        FlowScript re = new FlowScript();
        re.setCode(code);
        re.setPipeline(flowScript);
        return re;
    }

    public FlowScript flowScript(FlowNode flowScript) {
        setPipeline(flowScript);
        return this;
    }

    /***
     * 向容器注册
     * 如果没有code则为匿名临时script，不向容器注册
     * @return
     */
    public FlowScript register() {
        if (FlowUtils.isNotBlank(code)) {
            FlowContainer.addFlowConfig(this);
        }
        return this;
    }

    /***
     * 获取extension
     * @param context
     * @param extClass
     * @param <Ext>
     * @return
     */
    public <Ext extends IFlowActionExtension> Ext getExtension(FlowContext context, Class<Ext> extClass) {
        String extName = FlowHelper.getFlowExtName(extClass);
        Function<String, Ext> fetchExt = k -> {
            IFlowActionExtension ext = extensions.get(k);
            if (ext != null && FlowUtils.isClassOf(ext.getClass(), extClass)) {
                return (Ext) ext;
            }
            return null;
        };
        return handleKeySearch(context, extName, fetchExt);
    }

    /***
     * 获取属性
     * @param context
     * @param key
     * @return
     */
    public Object getExtProperty(FlowContext context, String key) {
        return handleKeySearch(context, key, k -> props.get(k));
    }

    /***
     * 获取属性或扩展
     * 分3层：
     * 1. 多action多级：    ${alias}#${key}
     * 2. action级别：     ${actionId}#${key}
     * 3. 全局：           ${key}
     * @param context
     * @param key
     * @param func
     * @param <T>
     * @return
     */
    private <T> T handleKeySearch(FlowContext context, String key, Function<String, T> func) {
        FlowNode flowNode = context.currentNode();
        String actionName = FlowContainer.getFlowAction(flowNode.getActionId()).actionId();
        String alias = flowNode.getAlias();
        List<String> keys = Lists.newLinkedList();
        if (FlowUtils.isNotBlank(alias)) {
            keys.add(String.format("%s#%s", alias, key));
        }
        keys.add(String.format("%s#%s", actionName, key));
        keys.add(key);
        for (String k : keys) {
            T re = func.apply(k);
            if (re != null) {
                return re;
            }
        }
        return null;
    }

    public FlowScript addProperty(String key, Object value) {
        props.put(key, value);
        return this;
    }

    public FlowScript addExtension(String key, IFlowActionExtension extension) {
        extensions.put(key, extension);
        return this;
    }

    public FlowScript addExtension(IFlowActionExtension extension) {
        String key = FlowHelper.getFlowExtName(extension.getClass());
        return addExtension(key, extension);
    }

    /**
     * Getter method for property   code.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property   code .
     *
     * @param code  value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter method for property   pipeline.
     *
     * @return property value of pipeline
     */
    public FlowNode getPipeline() {
        return pipeline;
    }

    /**
     * Setter method for property   pipeline .
     *
     * @param pipeline  value to be assigned to property pipeline
     */
    public void setPipeline(FlowNode pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * Getter method for property   props.
     *
     * @return property value of props
     */
    public Map<String, Object> getProps() {
        return props;
    }

    /**
     * Setter method for property   props .
     *
     * @param props  value to be assigned to property props
     */
    public void setProps(Map<String, Object> props) {
        this.props = props;
    }

    /**
     * Getter method for property   extensions.
     *
     * @return property value of extensions
     */
    public Map<String, IFlowActionExtension> getExtensions() {
        return extensions;
    }

    /**
     * Setter method for property   extensions .
     *
     * @param extensions  value to be assigned to property extensions
     */
    public void setExtensions(Map<String, IFlowActionExtension> extensions) {
        this.extensions = extensions;
    }
}