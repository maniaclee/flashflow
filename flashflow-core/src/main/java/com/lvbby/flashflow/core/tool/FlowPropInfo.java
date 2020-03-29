package com.lvbby.flashflow.core.tool;

import java.lang.reflect.Type;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowPropInfo.java, v 0.1 2020年03月29日 下午1:58 dushang.lp Exp $
 */
public class FlowPropInfo {
    private boolean global;
    private Class  actionClass;
    private String key;
    private Type   type;
    private String desc;
    private String typeName;

    /**
     * Getter method for property   global.
     *
     * @return property value of global
     */
    public boolean isGlobal() {
        return global;
    }

    /**
     * Setter method for property   global .
     *
     * @param global  value to be assigned to property global
     */
    public void setGlobal(boolean global) {
        this.global = global;
    }

    /**
     * Getter method for property   desc.
     *
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Setter method for property   desc .
     *
     * @param desc  value to be assigned to property desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
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
     * Getter method for property   key.
     *
     * @return property value of key
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter method for property   key .
     *
     * @param key  value to be assigned to property key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Getter method for property   type.
     *
     * @return property value of type
     */
    public Type getType() {
        return type;
    }

    /**
     * Setter method for property   type .
     *
     * @param type  value to be assigned to property type
     */
    public void setType(Type type) {
        this.typeName=type.getTypeName();
        this.type = type;
    }

    /**
     * Getter method for property   typeName.
     *
     * @return property value of typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Setter method for property   typeName .
     *
     * @param typeName  value to be assigned to property typeName
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}