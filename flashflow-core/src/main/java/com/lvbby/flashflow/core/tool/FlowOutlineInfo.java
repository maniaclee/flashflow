package com.lvbby.flashflow.core.tool;

import com.lvbby.flashflow.core.model.FlowActionInfo;
import com.lvbby.flashflow.core.model.FlowPropInfo;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowOutlineInfo.java, v 0.1 2020年03月29日 下午3:35 dushang.lp Exp $
 */
public class FlowOutlineInfo {
    public List<FlowPropInfo>   props;
    public List<FlowActionInfo> actions;

    /**
     * Getter method for property   props.
     *
     * @return property value of props
     */
    public List<FlowPropInfo> getProps() {
        return props;
    }

    /**
     * Setter method for property   props .
     *
     * @param props  value to be assigned to property props
     */
    public void setProps(List<FlowPropInfo> props) {
        this.props = props;
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