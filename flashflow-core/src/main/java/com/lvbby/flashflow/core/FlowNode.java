
package com.lvbby.flashflow.core;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lvbby.flashflow.core.utils.FlowUtils;
import com.lvbby.flashflow.core.utils.FlowHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowNode.java, v 0.1 2020年03月06日 下午5:11 dushang.lp Exp $
 */
public class FlowNode {
    /**
     * action的id
     * @see IFlowAction#actionId()
     */
    private String                 actionId;
    /** 节点名称，用作命名 */
    private String                 name;
    private Predicate<FlowContext> condition;
    private List<FlowNode>         children;
    /***
     * 多个action复用时通过alias区分
     */
    private String                 alias;

    private JSONObject props;

    public static FlowNode node(IFlowAction action) {
        return ofAlias(action, null);
    }

    public static FlowNode ofAlias(IFlowAction action, String alias) {
        String flowActionName = FlowHelper.getFlowActionName(action);
        FlowNode re = new FlowNode();
        re.setActionId(flowActionName);
        re.setAlias(alias);
        //注册Action,本地模式下action不会自动注册，这里插入
        FlowContainer.registerFlowAction(action);
        return re;
    }

    public static FlowNode ofArray(IFlowAction... actions) {
        FlowUtils.isTrue(actions != null && actions.length > 0, "action can't be empty");
        return ofList(Lists.newArrayList(actions));
    }

    public static FlowNode ofList(List<IFlowAction> actions) {
        FlowUtils.isTrue(FlowUtils.isNotEmpty(actions), "action can't be empty");
        FlowNode re = node(actions.get(0));
        FlowNode cur = re;
        for (int i = 1; i < actions.size(); i++) {
            FlowNode node = node(actions.get(i));
            cur.next(node);
            cur = node;
        }
        return re;
    }

    public void visit(Consumer<FlowNode> nodeConsumer){
        nodeConsumer.accept(this);
        if(children!=null){
            for (FlowNode child : children) {
                child.visit(nodeConsumer);
            }
        }
    }
    /***
     * build
     * @param nodes
     * @return
     */
    public FlowNode next(FlowNode... nodes) {
        if (children == null) {
            children = Lists.newLinkedList();
        }
        for (FlowNode node : nodes) {
            children.add(node);
        }
        return this;
    }

    public FlowNode prop(String key, Object value) {
        if (value != null) {
            if (props == null) {
                props = new JSONObject();
            }
            props.put(key, value);
        }
        return this;
    }
    public <T> FlowNode prop(FlowKey<T> key, T value) {
        if (value != null) {
            if (props == null) {
                props = new JSONObject();
            }
            props.put(key.getKey(), value);
        }
        return this;
    }

    /***
     * 快速
     * @param action
     * @return
     */
    public FlowNode next(IFlowAction action) {
        return next(node(action));
    }

    public FlowNode when(Predicate<FlowContext> condition) {
        setCondition(condition);
        return this;
    }

    public FlowNode alias(String alias) {
        setAlias(alias);
        return this;
    }

    /**
     * Getter method for property   actionId.
     *
     * @return property value of actionId
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Setter method for property   actionId .
     *
     * @param actionId  value to be assigned to property actionId
     */
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /**
     * Getter method for property   condition.
     *
     * @return property value of condition
     */
    public Predicate<FlowContext> getCondition() {
        return condition;
    }

    /**
     * Setter method for property   condition .
     *
     * @param condition  value to be assigned to property condition
     */
    public void setCondition(Predicate<FlowContext> condition) {
        this.condition = condition;
    }

    /**
     * Getter method for property   children.
     *
     * @return property value of children
     */
    public List<FlowNode> getChildren() {
        return children;
    }

    /**
     * Setter method for property   children .
     *
     * @param children  value to be assigned to property children
     */
    public void setChildren(List<FlowNode> children) {
        this.children = children;
    }

    /**
     * Getter method for property   alias.
     *
     * @return property value of alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Setter method for property   alias .
     *
     * @param alias  value to be assigned to property alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

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
     * Getter method for property   props.
     *
     * @return property value of props
     */
    public JSONObject getProps() {
        return props;
    }

    /**
     * Setter method for property   props .
     *
     * @param props  value to be assigned to property props
     */
    public void setProps(JSONObject props) {
        this.props = props;
    }
}