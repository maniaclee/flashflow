package com.lvbby.flashflow.core;

import com.google.common.collect.Maps;
import com.lvbby.flashflow.core.utils.FlowHelper;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version $Id: IFlowContext.java, v 0.1 2020年03月06日 下午5:03 dushang.lp Exp $
 */
public class FlowContext {

    protected FlowScript config;

    private String code;

    protected Map<String, Object> data;
    public static ThreadLocal<FlowContext> contextThreadLocal = new ThreadLocal<>();

    public FlowContext(String code) {
        this.code = code;
    }

    public FlowContext() {
    }

    public FlowContext script(FlowScript script) {
        setConfig(script);
        return this;
    }

    /**
     * 获取props的快速方法
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getProp(String key) {
        return (T) FlowHelper.getProp(key);
    }

    public <T> T getProp(String key, T defaultValue) {
        return (T) FlowHelper.getPropOrDefault(key, defaultValue);
    }

    public boolean hasProp(String key) {
        return FlowHelper.getProp(key) != null;
    }

    public <T> T getValue(String key) {
        return (T) ensureData().get(key);
    }

    public String getValueString(String key) {
        return getValue(key);
    }

    public FlowNode currentNode() {
        return get(FlowFrameWorkKeys.currentNode);
    }

    public boolean hasValue(String key) {
        return getValue(key) != null;
    }

    public <T> T get(FlowKey<T> key) {
        return getValue(key.getKey());
    }

    public <T> void put(FlowKey<T> key, T object) {
        putValue(key.getKey(), object);
    }

    private Map<String, Object> ensureData() {
        if (data == null) {
            data = Maps.newHashMap();
        }
        return data;
    }

    public FlowContext putValue(String key, Object value) {
        if (null != value && FlowUtils.isNotBlank(key)) {
            ensureData().put(key, value);
        }
        return this;
    }

    public static FlowContext currentContext() {
        return contextThreadLocal.get();
    }

    public static void cleanContext() {
        contextThreadLocal.remove();
    }

    public static void buildCurrentContext(FlowContext context) {
        contextThreadLocal.set(context);
    }

    /**
     * Getter method for property   config.
     *
     * @return property value of config
     */
    public FlowScript getConfig() {
        return config;
    }

    /**
     * Setter method for property   config .
     *
     * @param config  value to be assigned to property config
     */
    public void setConfig(FlowScript config) {
        this.config = config;
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
     * Getter method for property   data.
     *
     * @return property value of data
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * Setter method for property   data .
     *
     * @param data  value to be assigned to property data
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
