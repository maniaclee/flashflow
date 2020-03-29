package com.lvbby.flashflow.core.utils;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.lvbby.flashflow.core.Flow;
import com.lvbby.flashflow.core.error.FlowErrorCodeEnum;
import com.lvbby.flashflow.core.error.FlowException;
import groovy.lang.GroovyClassLoader;
import org.apache.commons.lang3.ClassUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowUtils.java, v 0.1 2020年03月12日 上午9:22 dushang.lp Exp $
 */
public class FlowUtils {

    /***
     * 扫描类
     * @param filter
     * @param packages
     * @return
     */
    public static List<Class> scan(Predicate<Class> filter, String... packages) {
        Set<Class> re = Sets.newHashSet();
        try {
            ClassPath path = ClassPath.from(Flow.class.getClassLoader());
            if (packages != null && packages.length > 0) {
                for (String aPackage : packages) {
                    ImmutableSet<ClassInfo> cls = path.getTopLevelClassesRecursive(aPackage);
                    if (cls != null) {
                        for (ClassInfo cl : cls) {
                            Class<?> clz = cl.load();
                            if (filter.apply(clz)) {
                                re.add(clz);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Lists.newArrayList(re);
    }

    public static <T> T groovyInstance(String script) {
        Class clazz = new GroovyClassLoader().parseClass(script);
        return (T) FlowUtils.newInstance(clazz);
    }

    public static Class groovyClass(String script) {
        return new GroovyClassLoader().parseClass(script);
    }

    public static String readResourceFile(String resource) {
        InputStreamReader in = new InputStreamReader(FlowUtils.class.getClassLoader().getResourceAsStream(resource));
        String result = new BufferedReader(in)
                .lines()
                .collect(Collectors.joining("\n"));
        return result;
    }

    public static <T> T newInstance(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("failed to instance %s", clz.getName()), e);
        }
    }

    public static String camel(String s) {
        if (isBlank(s)) {
            return s;
        }
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
    }

    public static boolean isClassOf(Class toCheck, Class target) {
        return ClassUtils.isAssignable(toCheck, target);
    }

    public static <A extends Annotation> A getAnnotation(Class clz, Class<A> annotation) {
        A re = (A) clz.getAnnotation(annotation);
        if (re != null) { return re; }
        re = ClassUtils.getAllInterfaces(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
                .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        if (re == null) {
            re = ClassUtils.getAllSuperclasses(clz).stream().filter(aClass -> aClass.isAnnotationPresent(annotation)).findAny()
                    .map(aClass -> aClass.getAnnotation(annotation)).orElse(null);
        }
        return re;
    }

    public static void isTrue(boolean expr, String msg) {
        if (!expr) {
            throw new FlowException(FlowErrorCodeEnum.systemError, msg);
        }
    }

    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        return regionMatches(cs1, false, 0, cs2, 0, cs1.length());
    }

    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The same check as in String.regionMatches():
            if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
                && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                return false;
            }
        }

        return true;
    }

}