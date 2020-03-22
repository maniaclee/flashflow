package com.lvbby.flashflow.test.local;

import com.alibaba.fastjson.JSON;
import com.lvbby.flashflow.core.FlowContext;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.anno.FlowAction;
import com.lvbby.flashflow.test.OrderDTO;

/**
 *
 * @author dushang.lp
 * @version $Id: InitAction.java, v 0.1 2020年03月06日 21:37 dushang.lp Exp $
 */
@FlowAction(id = "OrderProcessLocalAction")
public class OrderProcessLocalAction implements IFlowAction {
    @Override
    public void invoke(FlowContext context) {
        OrderDTO order = context.getValue("order");
        if(order==null || !order.getStatus().equals("init")){
            throw new RuntimeException("order is null or status invalid");
        }
        order.setStatus("finished");
        System.out.println("finish order: " + JSON.toJSONString(order,true));
    }

}