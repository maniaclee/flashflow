package com.lvbby.flowable.test.local;

import com.alibaba.fastjson.JSON;
import com.lvbby.flowable.core.FlowContext;
import com.lvbby.flowable.core.IFlowAction;
import com.lvbby.flowable.core.anno.FlowAction;
import com.lvbby.flowable.test.OrderDTO;

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