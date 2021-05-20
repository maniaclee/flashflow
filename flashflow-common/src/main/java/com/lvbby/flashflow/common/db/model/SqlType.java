package com.lvbby.flashflow.common.db.model;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.Map;

/**
 * Created by lipeng on 2017/1/6.
 */
public class SqlType {
    private static Map<String, Class> jdbc2javaMap = Maps.newHashMap();
    private static Map<Class, String> java2jdbcMap = Maps.newHashMap();

    static {
        jdbc2javaMap.put("BIT", Boolean.class);

        jdbc2javaMap.put("TINYINT", Integer.class);
        jdbc2javaMap.put("BOOLEAN", Integer.class);
        jdbc2javaMap.put("SMALLINT", Integer.class);
        jdbc2javaMap.put("MEDIUMINT", Integer.class);
        jdbc2javaMap.put("INTEGER", Integer.class);
        jdbc2javaMap.put("INT", Integer.class);

        jdbc2javaMap.put("BIGINT", Long.class);
        jdbc2javaMap.put("FLOAT", Float.class);
        jdbc2javaMap.put("DOUBLE", Double.class);
        jdbc2javaMap.put("DECIMAL", BigDecimal.class);

        jdbc2javaMap.put("DATE", Date.class);
        jdbc2javaMap.put("DATETIME", Date.class);
        jdbc2javaMap.put("TIMESTAMP", Date.class);
        jdbc2javaMap.put("TIME", Time.class);


        jdbc2javaMap.put("BINARY", byte[].class);
        jdbc2javaMap.put("VARBINARY", byte[].class);
        jdbc2javaMap.put("TINYBLOB", byte[].class);
        jdbc2javaMap.put("BLOB", byte[].class);
        jdbc2javaMap.put("MEDIUMBLOB", byte[].class);
        jdbc2javaMap.put("LONGBLOB", byte[].class);

        jdbc2javaMap.put("VARCHAR", String.class);
        jdbc2javaMap.put("CHAR", String.class);
        jdbc2javaMap.put("TEXT", String.class);


        java2jdbcMap.put(Integer.class, "INT");
        java2jdbcMap.put(int.class, "INT");
        java2jdbcMap.put(Short.class, "INT");
        java2jdbcMap.put(short.class, "INT");
        java2jdbcMap.put(Long.class, "BIGINT");
        java2jdbcMap.put(long.class, "BIGINT");
        java2jdbcMap.put(Float.class, "FLOAT");
        java2jdbcMap.put(float.class, "FLOAT");
        java2jdbcMap.put(Double.class, "DOUBLE");
        java2jdbcMap.put(double.class, "DOUBLE");
        java2jdbcMap.put(BigDecimal.class, "DECIMAL");
        java2jdbcMap.put(Date.class, "DATETIME");
        java2jdbcMap.put(String.class, "VARCHAR(256)");


    }

    public static Class<?> getJavaType(String sqlType) {
        sqlType = StringUtils.trimToNull(sqlType);
        return StringUtils.isBlank(sqlType) ? null : jdbc2javaMap.get(sqlType.toUpperCase());
    }

    public static String getJdbcType(Class clz) {
        Validate.notNull(clz,"failed to convert %s to jdbc type",clz);
        String s = java2jdbcMap.get(clz);
        Validate.notBlank(s,"unknown db type for : %s" , clz.getName());
        return s;
    }
}
