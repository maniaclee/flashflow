package com.lvbby.flashflow.core.config;

import com.lvbby.flashflow.core.FlowContext;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 使用groovy代理的Predict
 * @author dushang.lp
 * @version $Id: GroovyCondition.java, v 0.1 2020年03月24日 下午8:27 dushang.lp Exp $
 */
public class GroovyCondition implements Predicate<FlowContext> {

    private Predicate<FlowContext> groovyPredicate;
    private static     String beanScript = FlowUtils.readResourceFile("templates/GroovyCondition.template");
    private static AtomicInteger classNameCounter = new AtomicInteger(0);

    public GroovyCondition(String script) {
        String className = String.format("GC_%s", classNameCounter.incrementAndGet());
        String predictClassScript = beanScript.format(className, script);
        System.out.println(predictClassScript);
        /** 生成代理类 */
        this.groovyPredicate =FlowUtils.groovyInstance(predictClassScript);
    }

    @Override
    public boolean test(FlowContext t) {
        return groovyPredicate.test(t);
    }
}