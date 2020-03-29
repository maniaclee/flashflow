package com.lvbby.flashflow.core.model;

import java.util.Collection;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowStreamModel.java, v 0.1 2020年03月26日 下午4:20 dushang.lp Exp $
 */
public class FlowStreamModel {

    public String     key;
    public Collection targetlist;

    public FlowStreamModel(String key, Collection targetlist) {
        this.key = key;
        this.targetlist = targetlist;
    }
}