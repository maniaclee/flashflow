package com.lvbby.flashflow.core.tool;

import com.google.common.collect.Lists;
import com.lvbby.flashflow.core.FlowContainer;
import com.lvbby.flashflow.core.template.BeetlTemplateEngine;
import com.lvbby.flashflow.core.utils.FlowUtils;

/**
 * flow 常用工具
 * @author dushang.lp
 * @version $Id: FlowTool.java, v 0.1 2020年03月29日 下午1:45 dushang.lp Exp $
 */
public class FlowTool {

    /***
     * 生成flow的文档
     * @return
     */
    public static String createFlowDoc() {
        FlowOutlineInfo flowOutlineInfo = new FlowOutlineInfo();
        flowOutlineInfo.props = Lists.newArrayList(FlowContainer.propMetaInfo.values());
        flowOutlineInfo.actions = FlowContainer.actionMetaInfo;
        return new BeetlTemplateEngine(FlowUtils.readResourceFile("templates/README.md")).bind("src", flowOutlineInfo).render();
    }

}