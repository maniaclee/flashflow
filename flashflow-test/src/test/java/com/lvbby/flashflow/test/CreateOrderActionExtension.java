
package com.lvbby.flashflow.test;

import com.lvbby.flashflow.core.IFlowActionExtension;
import com.lvbby.flashflow.core.anno.FlowExt;

/**
 *
 * @author dushang.lp
 * @version $Id: InitActionExtension.java, v 0.1 2020年03月06日 22:12 dushang.lp Exp $
 */

@FlowExt("CreateOrderAction")
public interface CreateOrderActionExtension extends IFlowActionExtension {

     String getTitle();

}