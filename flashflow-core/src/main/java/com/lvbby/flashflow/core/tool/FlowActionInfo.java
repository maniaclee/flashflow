package com.lvbby.flashflow.core.tool;

import java.util.List;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowActionInfo.java, v 0.1 2020年03月29日 下午3:38 dushang.lp Exp $
 */
public class FlowActionInfo {
    public String name;
    public Class actionClass;
    public List<FlowPropInfo> props;

    /**
     * Getter method for property   name.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property   name .
     *
     * @param name  value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property   actionClass.
     *
     * @return property value of actionClass
     */
    public Class getActionClass() {
        return actionClass;
    }

    /**
     * Setter method for property   actionClass .
     *
     * @param actionClass  value to be assigned to property actionClass
     */
    public void setActionClass(Class actionClass) {
        this.actionClass = actionClass;
    }

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
}