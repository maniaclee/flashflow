package com.lvbby.flashflow.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

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

    /***
     * 默认值，生成文档时生成json config的默认值
     * @return
     */
    public String defaultValueJson(){
        return JSON.toJSONString(defaultValue(), SerializerFeature.WriteMapNullValue);
    }


    public Object defaultValue(){
        Type t = type;
        if(t instanceof ParameterizedType){
            t= ((ParameterizedType) t).getRawType();
        }
        if(t instanceof Class){
            Class clz = (Class) t;
            if(clz.equals(String.class)){
                return desc;
            }
            if(FlowUtils.isClassOf(clz,Number.class)){
                return 0;
            }
            if(FlowUtils.isClassOf(clz, Collection.class)){
                return Collections.emptyList();
            }
            if(FlowUtils.isClassOf(clz, Function.class)){
                return"groovy的Function函数，入参名为src";
            }
            return FlowUtils.newInstance(clz);
        }
        //提示作用
        return typeName;
    }

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