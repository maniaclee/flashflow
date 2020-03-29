package com.lvbby.flashflow.test.local;

import com.lvbby.flashflow.core.FlowContext;
import com.lvbby.flashflow.core.IFlowAction;
import com.lvbby.flashflow.core.anno.FlowAction;
import com.lvbby.flashflow.core.utils.FlowHelper;
import com.lvbby.flashflow.test.OrderDTO;
import com.lvbby.flashflow.test.CreateOrderActionExtension;

import java.util.Optional;

/**
 *
 * @author dushang.lp
 * @version $Id: CreateOrderLocalAction.java dushang.lp Exp $
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