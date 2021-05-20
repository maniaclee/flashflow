package com.lvbby.flashflow.common.db.model;

import com.google.common.base.CaseFormat;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lvbby.flashflow.common.util.CaseFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;

/**
 * Created by peng on 16/7/27.
 */
public class SqlTable {
    /**
     * Capital camel name like UserDetail
     */
    String name;
    String nameInDb;
    List<SqlColumn> fields = Lists.newLinkedList();
    private SqlColumn primaryKey;

    /**
     * 从Java class name解析
     * @param table
     * @return
     */
    public static SqlTable instance(String table) {
        table=table.replace("`","");
        SqlTable sqlTable = new SqlTable();
        sqlTable.setName(CaseFormatUtils.toCaseFormat(CaseFormat.UPPER_CAMEL,table));
        sqlTable.setNameInDb(CaseFormatUtils.toCaseFormat(CaseFormat.LOWER_UNDERSCORE,table));
        return sqlTable;
    }

    /***
     * 从db table的名称解析
     * @param table
     * @return
     */
    public static SqlTable dbName(String table) {
        table=table.replace("`","");
        SqlTable sqlTable = new SqlTable();
        sqlTable.setName(CaseFormatUtils.toCaseFormat(CaseFormat.UPPER_CAMEL,table));
        sqlTable.setNameInDb(table);
        return sqlTable;
    }


    public void buildPrimaryKeyField(String primaryKeyColumn) {
        if(StringUtils.isBlank(primaryKeyColumn))
            return;
        Function<String,String> format=input -> input.replaceAll("-|_","").toLowerCase();
        this.primaryKey = fields.stream().filter(f -> Objects.equals(format.apply(f.getNameInDb()), format.apply(primaryKeyColumn))).findFirst().orElse(null);
        if(this.primaryKey !=null){
            primaryKey.setPrimaryKey(true);
        }
    }

    public SqlColumn getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(SqlColumn primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getNameInDb() {
        return nameInDb;
    }

    public void setNameInDb(String nameInDb) {
        this.nameInDb = nameInDb;
    }

    public String getName() {
        return name;
    }

    public void setName(String tableName) {
        this.name = tableName;
    }

    public List<SqlColumn> getFields() {
        return fields;
    }

    public void setFields(List<SqlColumn> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
