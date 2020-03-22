package com.lvbby.flowable.test.local;

import com.lvbby.flowable.core.FlowContext;
import com.lvbby.flowable.core.IFlowAction;
import com.lvbby.flowable.core.anno.FlowAction;
import com.lvbby.flowable.core.utils.FlowHelper;
import com.lvbby.flowable.test.CreateOrderActionExtension;
import com.lvbby.flowable.test.OrderDTO;

import java.util.Optional;

/**
 *
 * @author dushang.lp
 * @version $Id: CreateOrderLocalAction.java, v 0.1 2020年03月09日 下午8:53 dushang.lp Exp $
 */
@FlowAction(id = "CreateOrderLocalAction")
public class CreateOrderLocalAction implements IFlowAction{
    @Override
    public void invoke(FlowContext context) {
        CreateOrderActionExtension extension = FlowHelper.getExtension(context, CreateOrderActionExtension.class);
        String title = Optional.ofNullable(extension).map(CreateOrderActionExtension::getTitle).orElse("default-title");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTitle(title);
        orderDTO.setStatus("init");
        context.putValue("order", orderDTO);
        System.out.println("init " + title);
    }
}