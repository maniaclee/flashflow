package com.lvbby.flashflow.core.utils;

/**
 *
 * @author dushang.lp
 * @version $Id: Templates.java, v 0.1 2020年03月25日 下午9:29 dushang.lp Exp $
 */
public enum  Templates {

    GroovyCondition;

    private String script;

    Templates() {
        String fileName = String.format("groovy/%s.template", name());
        this.script=FlowUtils.readResourceFile(fileName);
    }

    public String format(Object...args){
        if(args==null || args.length==0){
            return script;
        }
        return String.format(script, args);
    }

    public String getScript() {
        return script;
    }
}