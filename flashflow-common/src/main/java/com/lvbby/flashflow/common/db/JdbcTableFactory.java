package com.lvbby.flashflow.common.db;

import com.lvbby.flashflow.common.db.model.SqlTable;

import java.util.List;

/**
 * Created by dushang.lp on 2017/6/23.
 */
public interface JdbcTableFactory {
    List<SqlTable> getTables() throws Exception;
}
