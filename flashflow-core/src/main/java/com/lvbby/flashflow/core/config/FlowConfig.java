package com.lvbby.flashflow.core.config;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.flashflow.core.FlowScript;
import com.lvbby.flashflow.core.IFlowActionExtension;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowConfig.java, v 0.1 2020年03月23日 下午10:24 dushang.lp Exp $
 */
public class FlowConfig implements Serializable {

    /***
     * 全局prop
     */
    private JSONObject globalProps;

    /***
     * 全局扩展
     */
    private Map<String, IFlowActionExtension> globalExtensions;

    /***
     * 流程script
     */
    private List<FlowScript> scripts;

    /**
     * Getter method for property   globalProps.
     *
     * @return property value of globalProps
     */
    public JSONObject getGlobalProps() {
        return globalProps;
    }

    /**
     * Setter method for property   globalProps .
     *
     * @param globalProps  value to be assigned to property globalProps
     */
    public void setGlobalProps(JSONObject globalProps) {
        this.globalProps = globalProps;
    }

    /**
     * Getter method for property   globalExtensions.
     *
     * @return property value of globalExtensions
     */
    public Map<String, IFlowActionExtension> getGlobalExtensions() {
        return globalExtensions;
    }

    /**
     * Setter method for property   globalExtensions .
     *
     * @param globalExtensions  value to be assigned to property globalExtensions
     */
    public void setGlobalExtensions(Map<String, IFlowActionExtension> globalExtensions) {
        this.globalExtensions = globalExtensions;
    }

    /**
     * Getter method for property   scripts.
     *
     * @return property value of scripts
     */
    public List<FlowScript> getScripts() {
        return scripts;
    }

    /**
     * Setter method for property   scripts .
     *
     * @param scripts  value to be assigned to property scripts
     */
    public void setScripts(List<FlowScript> scripts) {
        this.scripts = scripts;
    }
}