package com.lvbby.flashflow.test.spring;

import com.lvbby.flashflow.test.AbstractOrderAction;
import com.lvbby.flashflow.test.CreateOrderActionExtension;
import com.lvbby.flashflow.test.OrderContext;
import com.lvbby.flashflow.test.OrderDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 * @author dushang.lp
 * @version $Id: InitAction.java, v 0.1 2020年03月06日 21:37 dushang.lp Exp $
 */
@Component
public class CreateOrderAction extends AbstractOrderAction {

    public static final String PROP          = "title";
    public static final String PROP_DELEGATE = "createOrder.delegate.prop";

    @Override
    public void invoke(OrderContext context) {

        CreateOrderActionExtension extension = getExtension(CreateOrderActionExtension.class);
        String title = Optional.ofNullable(extension).map(CreateOrderActionExtension::getTitle).orElse("default-title");
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTitle(title);
        orderDTO.setStatus("init");
        context.putValue("order", orderDTO);
        System.out.println("init " + title);

        System.out.println("========props=====:" + getPropOrDefault(PROP, "null-default"));
    }

}