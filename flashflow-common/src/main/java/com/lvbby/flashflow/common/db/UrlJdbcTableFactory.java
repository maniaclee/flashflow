package com.lvbby.flashflow.common.db;

import com.google.common.collect.Lists;
import com.lvbby.flashflow.common.db.model.SqlColumn;
import com.lvbby.flashflow.common.db.model.SqlTable;
import com.lvbby.flashflow.common.db.model.SqlType;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by peng on 16/7/27.
 */
public class UrlJdbcTableFactory implements JdbcTableFactory {
    Connection conn = null;
    private String jdbcUrl;
    private String user;
    private String password;
    private String tableRegularFilter;

    public static UrlJdbcTableFactory of(String jdbcUrl, String user, String password) {
        UrlJdbcTableFactory re = new UrlJdbcTableFactory();
        re.setJdbcUrl(jdbcUrl);
        re.setUser(user);
        re.setPassword(password);
        return re;
    }

    public static UrlJdbcTableFactory of(URI uri) {
        UrlJdbcTableFactory re = new UrlJdbcTableFactory();
        re.setJdbcUrl(uri.toString());
        return re;
    }

    public UrlJdbcTableFactory tableRegularFilter(String tableRegularFilter) {
        this.tableRegularFilter = tableRegularFilter;
        return this;
    }

    @Override
    public List<SqlTable> getTables() throws Exception {
        init();
        List<SqlTable> re = Lists.newLinkedList();
        for (String s : getTableNames()) {
            SqlTable instance = SqlTable.dbName(s);
            instance.setFields(getFields(instance));
            instance.buildPrimaryKeyField(getPrimaryKey(instance.getNameInDb()));
            re.add(instance);
        }
        return re;
    }

    private String getPrimaryKey(String table) throws SQLException {
        ResultSet pkRSet = conn.getMetaData().getPrimaryKeys(null, null, table);
        while (pkRSet.next()) {
            return pkRSet.getObject(4).toString();
        }
        return null;
    }

    private void init() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(getJdbcUrl(), getUser(), getPassword());
    }

    private List<String> getTableNames() throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        List<String> re = Lists.newLinkedList();
        while (rs.next()) re.add(rs.getString(3));
        if (StringUtils.isNotBlank(tableRegularFilter) ) {
            re=re.stream().filter(s -> s.matches(tableRegularFilter)).collect(Collectors.toList());
        }
        return re;
    }

    private List<SqlColumn> getFields(SqlTable SqlTable) throws SQLException {
        List<SqlColumn> re = Lists.newArrayList();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getColumns(null, null, SqlTable.getNameInDb(), "%");
        while (rs.next()) {
            SqlColumn f = SqlColumn.instance(rs.getString(4));
            //            f.setDbType(rs.getString(5));
            f.setDbType(rs.getString(6));
            f.setJavaType(SqlType.getJavaType(f.getDbType()));
            f.setComment(rs.getString(12));
            // f.setNameInDb(rs.getString("COLUMN_NAME"));
            // f.setDbType(rs.getString("TYPE_NAME"));
            re.add(f);
        }
        return re;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
