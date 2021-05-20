/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core.model;

import com.google.common.collect.Maps;
import com.lvbby.flashflow.core.util.FlowCommonUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author dushang.lp
 * @version : FlowConfig.java, v 0.1 2021年05月14日 下午10:19 dushang.lp Exp $
 */
public class FlowConfig {

    private String name;

    /**
     * flow id
     */
    private String     id;
    /**
     * start node of the process
     */
    private List<Node> flow;

    private Map<String, Node> _nodeCache = Maps.newHashMap();

    public void init() {
        if (FlowCommonUtils.isNotEmpty(flow)) {
            _nodeCache = flow.stream().collect(Collectors.toMap(Node::getId, Function.identity()));
        }
    }

    /**
     * visit all nodes order by process graph
     * @param consumer
     */
    public void visitNode(Consumer<Node> consumer) {
        doVisitNode(flow.get(0), consumer);
    }

    private void doVisitNode(Node node, Consumer<Node> consumer) {
        if (node == null) {
            return;
        }
        consumer.accept(node);
        if (node.hasNext()) {
            node.getNext().stream().map(s -> getNode(s.getNode())).filter(Objects::nonNull).forEach(n -> doVisitNode(n, consumer));
        }
    }

    public Node getNode(String nodeId) {
        return _nodeCache.get(nodeId);
    }

    /**
     * Getter method for property <tt>name</tt>.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     *
     * @param name  value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>code</tt>.
     *
     * @return property value of code
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for property <tt>code</tt>.
     *
     * @param id  value to be assigned to property code
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>flow</tt>.
     *
     * @return property value of flow
     */
    public List<Node> getFlow() {
        return flow;
    }

    /**
     * Setter method for property <tt>flow</tt>.
     *
     * @param flow  value to be assigned to property flow
     */
    public void setFlow(List<Node> flow) {
        this.flow = flow;
    }
}