
package com.lvbby.flashflow.test.local;

import com.lvbby.flashflow.core.Flow;
import com.lvbby.flashflow.core.FlowNode;
import com.lvbby.flashflow.core.FlowScript;
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
public class flashflowLocalExample {
    CreateOrderLocalAction  initAction    = new CreateOrderLocalAction();
    OrderProcessLocalAction processAction = new OrderProcessLocalAction();

    /***
     * 快速流程
     * @throws Exception
     */
    @Test
    public void quickScript() throws Exception {
        Flow.exec(new OrderContext()
                .script(FlowScript.anonymous(FlowNode.ofArray(initAction, processAction)))
        );
    }

    @Test
    public void demo() {

        /** 1. 定义流程编排*/
        FlowNode pipeline = node(initAction)
                .next(node(processAction)
                        .when(ctx -> FlowUtils.equals(ctx.getValueString("props"), "test"))
                );

        /** 2. 定义场景：流程+扩展点 */
        FlowScript.of("example", pipeline).register();

        FlowScript.of("pipeline", pipeline)
                .addExtension((CreateOrderActionExtension) () -> "test title")
                .register()
        ;

        /** 启动流程 */
        Flow.exec(new OrderContext("example"));
        Flow.exec(new OrderContext("pipeline").putValue("props", "test"));
    }

}