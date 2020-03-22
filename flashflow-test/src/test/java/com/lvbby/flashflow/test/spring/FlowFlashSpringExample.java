
package com.lvbby.flashflow.test.spring;

import com.alibaba.fastjson.JSON;
import com.lvbby.flashflow.core.Flow;
import com.lvbby.flashflow.core.FlowNode;
import com.lvbby.flashflow.core.FlowScript;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.utils.FlowHelper;
import com.lvbby.flashflow.spring.anno.EnableFlow;
import com.lvbby.flashflow.test.CreateOrderActionExtension;
import com.lvbby.flashflow.test.spring.FlowFlashSpringExample.ConfigurationTest;
import com.lvbby.flashflow.test.OrderContext;
import com.lvbby.flashflow.test.OrderDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * flow流程引擎示例
 * @author dushang.lp
 * @version $Id: test.java, v 0.1 2020年03月07日 15:23 dushang.lp Exp $
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigurationTest.class)
public class FlowFlashSpringExample {

    @Resource
    IFlowAction createOrderActionInner;
    @Resource
    IFlowAction orderProcessActionInner;

    @Autowired
    private CreateOrderAction createOrderAction;
    @Autowired
    OrderProcessAction orderProcessAction;


    /***
     * 配置
     */
    @ComponentScan(basePackages = "com.lvbby")
    @Configuration
    @EnableFlow
    public static class ConfigurationTest {
        @Bean
        public IFlowAction createOrderActionInner() {
            return context -> {
                CreateOrderActionExtension extension = FlowHelper.getExtension(context, CreateOrderActionExtension.class);
                String title = Optional.ofNullable(extension).map(CreateOrderActionExtension::getTitle).orElse("default-title");
                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setTitle(title);
                orderDTO.setStatus("init");
                context.putValue("order", orderDTO);
                System.out.println("init " + title);
            };
        }

        @Bean
        public IFlowAction orderProcessActionInner() {
            return context -> {
                OrderDTO order = context.getValue("order");
                if (order == null || !order.getStatus().equals("init")) {
                    throw new RuntimeException("order is null or status invalid");
                }
                order.setStatus("finished");
                System.out.println("finish order: " + JSON.toJSONString(order, true));
            };
        }
    }

    /***
     * 最简单的pipeline
     */
    @Test
    public void simplePipeline() {
        Flow.execSimple(new OrderContext(), createOrderAction, orderProcessAction);
    }

    @Test
    public void pipelineWithScene() {
        /** 1. 创建流程 */
        FlowNode pipeline = FlowNode.ofArray(createOrderAction, orderProcessAction);

        /** 2. 创建场景，实现业务扩展逻辑，并注册到框架里*/
        FlowScript.of("pipelineSingle", pipeline)
                .addExtension((CreateOrderActionExtension) () -> "pipelineATitle").register();

        /** 3. 按照场景进行调用 */
        Flow.exec(new OrderContext("pipelineSingle"));
    }

    @Test
    public void pipelineMultiScene() {
        FlowNode pipeline = FlowNode.ofArray(createOrderAction, orderProcessAction);

        /** 创建两个场景，具有不同的业务逻辑实现*/
        FlowScript.of("pipelineA", pipeline)
                .addExtension((CreateOrderActionExtension) () -> "pipelineATitle").register();

        FlowScript.of("pipelineB", pipeline)
                .addExtension((CreateOrderActionExtension) () -> "pipelineBTitle")
                .register();

        /** 实际调用两个场景 */
        Flow.exec(new OrderContext("pipelineA"));
        Flow.exec(new OrderContext("pipelineB"));
    }

    @Test
    public void innerBeanTest() {

        /** 1. 定义流程编排*/
        FlowNode pipeline =
                FlowNode.node(createOrderActionInner)
                        .next(FlowNode.node(orderProcessActionInner).when(ctx -> ctx.hasValue("order")));

        /** 2. 定义场景：流程+扩展点 */
        FlowScript.of("example", pipeline).register();
        FlowScript.of("error", FlowNode.node(orderProcessActionInner));
        /** 启动流程 */
        Flow.exec(new OrderContext("example"));
        System.out.println("=================");
        try {
            Flow.exec(new OrderContext("error"));
        } catch (Exception e) {
            System.out.println("expected error");
        }
    }

    @Test
    public void normalBeanTest() {

        /** 1. 定义流程编排*/
        FlowNode pipeline = FlowNode.node(createOrderAction)
                .next(FlowNode.node(orderProcessAction).when(ctx -> ctx.hasValue("order")));

        /** 2. 定义场景：流程+扩展点 */
        FlowScript.of("exampleBean", pipeline)
                .addExtension((CreateOrderActionExtension) () -> "springOrder")
                .addProperty(CreateOrderAction.PROP, "PROPERTY--TEST")
                .addProperty(CreateOrderAction.PROP_DELEGATE, "delegateExtProperty!!!!!")
                .register();

        /** 启动流程 */
        Flow.exec(new OrderContext("exampleBean"));
    }
}