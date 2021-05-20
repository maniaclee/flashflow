package com.lvbby.flashflow.common.db;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.statement.NotNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSQLColumnDefinition;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lvbby.flashflow.common.db.model.SqlColumn;
import com.lvbby.flashflow.common.db.model.SqlTable;
import com.lvbby.flashflow.common.db.model.SqlType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by lipeng on 2017/1/6.
 */
public class SqlParser {

    public static List<SqlTable> fromSql(String sql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        if (stmtList == null)
            return Lists.newArrayList();
        return stmtList.stream().filter(s -> s instanceof MySqlCreateTableStatement).map(sqlStatement -> {
            MySqlCreateTableStatement create = (MySqlCreateTableStatement) sqlStatement;
            SqlTable t = SqlTable.dbName(create.getTableSource().toString());
            t.setFields(create.getTableElementList().stream().filter(s -> s instanceof MySqlSQLColumnDefinition).map(sqlTableElement -> buildColumn((MySqlSQLColumnDefinition) sqlTableElement)).collect(Collectors.toList()));
            t.setPrimaryKey(t.getFields().stream().filter(sqlColumn -> sqlColumn.isPrimaryKey()).findAny().orElse(null));
            return t;
        }).collect(Collectors.toList());
    }

    private static SqlColumn buildColumn(MySqlSQLColumnDefinition column) {
        SqlColumn sqlColumn = SqlColumn.instance(column.getName().getSimpleName());
        sqlColumn.setComment(Optional.ofNullable(column.getComment()).map(sqlExpr -> sqlExpr.toString()).orElse(null));
        sqlColumn.setNullable(!hasConstrain(column, NotNullConstraint.class));
        sqlColumn.setPrimaryKey(hasConstrain(column, SQLColumnPrimaryKey.class));
        sqlColumn.setUnique(hasConstrain(column, SQLColumnUniqueKey.class));
        sqlColumn.setDbType(column.getDataType().getName());
        sqlColumn.setJavaType(SqlType.getJavaType(sqlColumn.getDbType()));
        Validate.notNull(sqlColumn.getJavaType() == null, "unknown sql type : " + sqlColumn.getDbType());
        return sqlColumn;
    }

    public static List<JSONObject> parseInsert(String s) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(s, JdbcConstants.MYSQL);
        return sqlStatements.stream().map(sqlStatement -> {
            Validate.isTrue(sqlStatement instanceof SQLInsertStatement, "必须是insert语句");
            SQLInsertStatement insertStatement = (SQLInsertStatement) sqlStatement;
            JSONObject re = new JSONObject();
            for (int i = 0; i < insertStatement.getColumns().size(); i++) {
                re.put(parseColumn(insertStatement.getColumns().get(i).toString()),parse(insertStatement.getValues().getValues().get(i)));
            }
            return re;
        }).collect(Collectors.toList());
    }

    private static String parseColumn(String s) {
        s = StringUtils.trimToEmpty(s);
        if (!s.isEmpty()) {
            if (s.startsWith("`") || s.startsWith("'") || s.startsWith("\"")) {
                s = s.substring(1);
            }
            if (s.endsWith("`") || s.endsWith("'") || s.endsWith("\"")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    private static Object parse(SQLExpr sqlExpr) {
        if (sqlExpr instanceof SQLIntegerExpr) {
            return ((SQLIntegerExpr) sqlExpr).getNumber();
        }
        if (sqlExpr instanceof SQLCharExpr) {
            String text = ((SQLCharExpr) sqlExpr).getText();
            try {
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(text);
            } catch (ParseException e) {
                return text;
            }
        }
        if (sqlExpr instanceof SQLNullExpr)
            return null;
        return null;

    }


    private static boolean hasConstrain(MySqlSQLColumnDefinition mySqlSQLColumnDefinition, Class<?> clz) {
        return mySqlSQLColumnDefinition.getConstraints().stream().anyMatch(sqlColumnConstraint -> clz.isAssignableFrom(sqlColumnConstraint.getClass()));
    }
}
