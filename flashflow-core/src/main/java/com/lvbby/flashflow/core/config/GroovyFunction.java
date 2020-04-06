package com.lvbby.flashflow.core.config;

import com.alibaba.fastjson.JSONObject;
import com.lvbby.flashflow.core.utils.FlowUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 使用groovy代理的Predict
 * @author dushang.lp
 * @version $Id: GroovyCondition.java, v 0.1 2020年03月24日 下午8:27 dushang.lp Exp $
 */
public class GroovyFunction implements Function {

    private Function instance;
    private static AtomicInteger classNameCounter = new AtomicInteger(0);

    private static     String script = FlowUtils.readResourceFile("templates/GroovyFunction.template");

    public GroovyFunction(Type functionType, String script) {
        String className = String.format("GFunction_%s", classNameCounter.incrementAndGet());
        FlowUtils.isTrue(functionType instanceof ParameterizedType,"invalid functionType");

        Type[] actualTypeArguments = ((ParameterizedType) functionType).getActualTypeArguments();
        String scriptContent = FlowUtils.renderTemplateByScript(GroovyFunction.script,
                new JSONObject()
                        .fluentPut("packageName", "com.lvbby.flashflow._groovbyGen")
                        .fluentPut("functionName", className)
                        .fluentPut("script", script)
                        .fluentPut("fromType", actualTypeArguments[0].getTypeName())
                        .fluentPut("toType", actualTypeArguments[1].getTypeName())
        );
        /** 生成代理类 */
        this.instance =FlowUtils.groovyInstance(scriptContent);
    }


    @Override
    public Object apply(Object o) {
        return instance.apply(o);
    }
}