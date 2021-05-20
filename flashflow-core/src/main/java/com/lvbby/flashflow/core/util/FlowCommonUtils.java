/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package com.lvbby.flashflow.core;

import java.util.Collection;

/**
 *
 * @author dushang.lp
 * @version : Utils.java, v 0.1 2021年05月16日 下午2:45 dushang.lp Exp $
 */
public class FlowCommonUtils {

    public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
                .append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1))
                .toString();
    }
    public static boolean isNotBlank(String s){
        return s!=null && !s.trim().isEmpty();
    }

    public static boolean isNotEmpty(Collection c){
        return c!=null && !c.isEmpty();
    }


}