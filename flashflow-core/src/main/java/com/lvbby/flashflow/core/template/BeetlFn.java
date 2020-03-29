package com.lvbby.flashflow.core.template;

import com.lvbby.flashflow.core.utils.FlowUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/1/7.
 */
public class BeetlFn extends StringUtils{

    public List<Field>  getFields(Class clz){
        return Arrays.stream(clz.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()) && !f.getType().getName().startsWith("groovy"))
                .collect(Collectors.toList());
    }

    public boolean isEmpty(Collection e) {
        return FlowUtils.isEmpty(e);
    }
    public boolean isNotEmpty(Collection e) {
        return FlowUtils.isNotEmpty(e);
    }

    public static String camel(String s) {
        return FlowUtils.camel(s);
    }
    public boolean isPrimitiveBoolean(Class clz){
        return clz.getSimpleName().equals("boolean");
    }

    public String capital(String s) {
        return StringUtils.capitalize(s);
    }

    public String unCapital(String s) {
        return StringUtils.uncapitalize(s);
    }

    public static String getJavaClassName(String className) {
        return className.replaceAll("[^.<>]+\\.", "");
    }

    public static String getPackage(String className) {
        if (!className.contains(".")) {
            return "";
        }
        return className.replaceAll("(\\.[^.]+)$", "");
    }

    public static String format(String format,Object... args){
        return String.format(format,args);
    }

}
