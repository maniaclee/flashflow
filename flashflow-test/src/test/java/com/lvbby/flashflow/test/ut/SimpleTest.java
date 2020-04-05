package com.lvbby.flashflow.test.ut;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.lvbby.flashflow.core.FlowContext;
import com.lvbby.flashflow.core.config.FlowConfig;
import com.lvbby.flashflow.core.config.FlowConfigParser;
import com.lvbby.flashflow.core.config.GroovyCondition;
import com.lvbby.flashflow.test.CreateOrderActionExtension;
import groovy.lang.GroovyClassLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;

import java.io.FileInputStream;

/**
 *
 * @author dushang.lp
 * @version $Id: SimpleTest.java, v 0.1 2020年03月24日 下午5:44 dushang.lp Exp $
 */
public class SimpleTest {

    @Test
    public void groovy() throws Exception {
        String script = IOUtils.toString(new FileInputStream(
                "/Users/dushang.lp/workspace/project/flashflow/flashflow-test/src/test/java/com/lvbby/flashflow/test/ut/GroovyExtension.groovy"));

        Class clazz = new GroovyClassLoader().parseClass(script);
        CreateOrderActionExtension o = (CreateOrderActionExtension) clazz.newInstance();
        System.out.println(o.getTitle());

    }

    private String script() throws Exception {
        return  IOUtils.toString(new FileInputStream(
                "/Users/dushang.lp/workspace/project/flashflow/flashflow-test/src/test/java/com/lvbby/flashflow/test/ut/GroovyExtension.groovy"));
    }
    @Test
    public void groovyCondition() throws Exception {
        String s = "context !=null && context.getValue('testKey') !=null ";
        GroovyCondition init = new GroovyCondition(s);

        System.out.println(init.test(null));

        FlowContext t = new FlowContext();
        System.out.println(init.test(t));

        t.putValue("testKey", Maps.newHashMap());
        System.out.println(init.test(t));
    }

    @Test
    public void config() throws Exception {
        String configString = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("flowConfig.json"));
        String extension = script();
        configString = configString.replace("\"XX\"", String.format("'%s'", extension));
        System.out.println(configString);

        FlowConfig flowScript = FlowConfigParser.parseJson(configString);
        CreateOrderActionExtension ex = (CreateOrderActionExtension) flowScript.getScripts().get(0).getExtensions().get("CreateOrderActionExt");
        System.out.println(ex.getTitle());
        System.out.println(JSON.toJSONString(flowScript,true));
    }

    @Test
    public void scan() throws Exception {
        System.out.println(ClassUtils.wrapperToPrimitive(Class.class).getName());
        System.out.println(ClassUtils.wrapperToPrimitive(Integer.class).getName());
    }
}