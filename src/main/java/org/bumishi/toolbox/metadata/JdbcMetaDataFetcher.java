package org.bumishi.toolbox.metadata;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang.xie
 * @date 2016/9/20
 */
public class JdbcMetaDataFetcher {

    private DataSource dataSource;


    public JdbcMetaDataFetcher(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public TableInfo getTable(String tableName) {
        Connection conn = null;
        ResultSet tableSet = null;
        ResultSet columnSet = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            //获取表
            tableSet = databaseMetaData.getTables(null, "%", tableName, new String[]{"TABLE"});
            TableInfo tableInfo = new TableInfo(tableSet.getString("TABLE_NAME"), tableSet.getString("REMARKS"));
            //获取tableName表列信息
            columnSet = databaseMetaData.getColumns(null, "%", tableName, "%");
            tableInfo.setColumnList(getColumnList(columnSet));
            return tableInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (columnSet != null) {
                try {
                    columnSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (tableSet != null) {
                try {
                    tableSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public List<TableInfo> getAllTables() {
        Connection conn = null;
        ResultSet tableSet = null;
        ResultSet columnSet = null;
        List<TableInfo> tableInfos = new ArrayList<>();
        try {

            conn = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            //获取表
            tableSet = databaseMetaData.getTables(null, "%", "%", new String[]{"TABLE"});
            while (tableSet.next()) {
                TableInfo tableInfo = new TableInfo(tableSet.getString("TABLE_NAME"), tableSet.getString("REMARKS"));
                //获取tableName表列信息
                columnSet = databaseMetaData.getColumns(null, "%", tableInfo.getTableName(), "%");
                tableInfo.setColumnList(getColumnList(columnSet));
                tableInfos.add(tableInfo);
                columnSet.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (columnSet != null) {
                try {
                    columnSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (tableSet != null) {
                try {
                    tableSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tableInfos;
    }

    private List<ColumnInfo> getColumnList(ResultSet columnSet) throws Exception {
        List<ColumnInfo> list = new ArrayList<>();
        while (columnSet.next()) {
            String columnName = columnSet.getString("COLUMN_NAME");
            String columnComment = columnSet.getString("REMARKS");
            int sqlType = columnSet.getInt("DATA_TYPE");
            list.add(new ColumnInfo(columnName, columnComment, sqlType));
        }
        return list;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
