
package com.lvbby.flashflow.test.local;

import com.lvbby.flashflow.core.Flow;
import com.lvbby.flashflow.core.FlowContext;
import com.lvbby.flashflow.core.FlowNode;
import com.lvbby.flashflow.core.FlowScript;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.utils.FlowUtils;
import com.lvbby.flashflow.test.OrderContext;
import com.lvbby.flashflow.test.CreateOrderActionExtension;
import org.junit.Test;

import static com.lvbby.flashflow.core.FlowNode.node;

/**
 *
 * @author dushang.lp
 * @version $Id: flashflowExample.java, v 0.1 2020年03月06日 21:33 dushang.lp Exp $
 */
public class FlashflowLocalExample {
    CreateOrderLocalAction  initAction    = new CreateOrderLocalAction();
    OrderProcessLocalAction processAction = new OrderProcessLocalAction();


    private IFlowAction startAction = new IFlowAction() {

        @Override
        public void invoke(FlowContext context) {
            Object simpleValue = context.getValue("simpleValue");
            System.out.println("start:"+simpleValue);
        }
    };

    private IFlowAction endAction = new IFlowAction() {

        @Override
        public void invoke(FlowContext context) {
            System.out.println("end");
        }
    };
    /***
     * 快速流程
     * @throws Exception
     */
    @Test
    public void quickScript() throws Exception {
        Flow.execSimple(new OrderContext(),startAction, endAction);
        Flow.exec(new OrderContext(),FlowNode.ofArray(startAction, endAction));
    }

    @Test
    public void quickScriptWithProp() throws Exception {
        /** 构建上下文 */
        OrderContext context = new OrderContext();
        context.putValue("simpleValue", "this is a test");//传入扩展属性，startAction会获取

        Flow.execSimple(context,startAction, endAction);
    }

    @Test
    public void nodeDemo() {

        /** 1. 定义流程编排*/
        FlowNode pipeline = FlowNode.node(initAction)
                .next(node(processAction) );

        /** 2. 定义场景：场景名称+流程编排 */
        FlowScript.of("example", pipeline).register();

        /** 启动流程：按照场景名称进行触发 */
        Flow.exec(new OrderContext("example"));
    }
    @Test
    public void conditionDemo() {

        /** 1. 定义流程编排*/
        FlowNode pipeline = FlowNode.node(initAction)
                .next(node(processAction)
                        .when(ctx -> FlowUtils.equals(ctx.getValueString("props"), "test"))
                );

        /** 2. 定义场景：流程+扩展点 */
        FlowScript.of("pipeline", pipeline)
                .register()
        ;

        /** 启动流程 */
        Flow.exec(new OrderContext("pipeline").putValue("props", "test"));
        Flow.exec(new OrderContext("pipeline").putValue("props", "not match property"));
    }
    @Test
    public void demo() {

        /** 1. 定义流程编排*/
        FlowNode pipeline = FlowNode.node(initAction)
                .next(node(processAction).when(ctx -> FlowUtils.equals(ctx.getValueString("props"), "test"))
                );

        /** 2. 定义场景：流程+扩展点 */
        FlowScript.of("pipeline", pipeline)
                .addExtension((CreateOrderActionExtension) () -> "test title")//
                .register();

        /** 启动流程 */
        Flow.exec(new OrderContext("pipeline").putValue("props", "test"));
        Flow.exec(new OrderContext("pipeline").putValue("props", "invalid props"));
    }

}