package com.lvbby.flashflow.core.template;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lipeng on 16/7/28.
 */
public class BeetlTemplateEngine   {
    Template t;
    GroupTemplate gt;
    private static final BeetlFn funcPackage = new BeetlFn();

    public BeetlTemplateEngine(String context) {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        try {
            Configuration cfg = Configuration.defaultConfiguration();
            gt = new GroupTemplate(resourceLoader, cfg);
            gt.registerFunctionPackage("utils",funcPackage);
            t = gt.getTemplate(context);
        } catch (IOException e) {
            throw new RuntimeException("error create template ", e);
        }
    }

    public BeetlTemplateEngine registFunction(String pack, Class clz) {
        gt.registerFunctionPackage(pack, clz);
        return this;
    }

    public GroupTemplate getGroupTemplate() {
        return gt;
    }

    public BeetlTemplateEngine bind(String key, Object obj) {
        t.binding(key, obj);
        return this;
    }

    public void binding(Map map) {t.binding(map);}

    public String render() {
        return t.render();
    }


}
