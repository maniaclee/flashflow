package com.lvbby.flashflow.core.tool;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowOutlineInfo.java, v 0.1 2020年03月29日 下午3:35 dushang.lp Exp $
 */
public class FlowOutlineInfo {
    public List<FlowPropInfo> globalProps;
    public List<FlowActionInfo> actions;

    /**
     * Getter method for property   globalProps.
     *
     * @return property value of globalProps
     */
    public List<FlowPropInfo> getGlobalProps() {
        return globalProps;
    }

    /**
     * Setter method for property   globalProps .
     *
     * @param globalProps  value to be assigned to property globalProps
     */
    public void setGlobalProps(List<FlowPropInfo> globalProps) {
        this.globalProps = globalProps;
    }

    /**
     * Getter method for property   actions.
     *
     * @return property value of actions
     */
    public List<FlowActionInfo> getActions() {
        return actions;
    }

    /**
     * Setter method for property   actions .
     *
     * @param actions  value to be assigned to property actions
     */
    public void setActions(List<FlowActionInfo> actions) {
        this.actions = actions;
    }
}