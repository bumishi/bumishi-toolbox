package org.bumishi.toolbox.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang.xie
 * @date 2016/9/20
 */
public class TableInfo {

    private String tableName;

    private String tableComment;

    private List<ColumnInfo> columnList = new ArrayList<>();

    private String insertAllColumnSql;

    private String insertNoIdSql;

    private String selectAllSql;

    private String updateByIdSql;

    private String deleteByIdSql;

    private String idRealName;//id字段在数据库的真实名称，主要是处理大小写问题

    public TableInfo(String tableName, String tableComment) {
        this.tableName = tableName;
        this.tableComment = tableComment;
    }

    public String getTableName() {
        return tableName;
    }


    public String getTableComment() {
        return tableComment;
    }

    public List<ColumnInfo> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<ColumnInfo> columnList) {
        this.columnList = columnList;
        for (ColumnInfo columnInfo : columnList) {
            if (columnInfo.getColumnName().equalsIgnoreCase("id")) {
                idRealName = columnInfo.getColumnName();
                break;
            }
        }
//        this.selectAllSql=getSelect();
//        this.insertAllColumnSql=getInsert(true);
//        this.insertNoIdSql=getInsert(false);
//        this.updateByIdSql=getUpdate();
//        this.deleteByIdSql=getDelete();

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("/*" + tableComment + "*/->" + tableName + "\n");
        s.append("colums:\n");
        columnList.stream().forEach(columnInfo -> {
            s.append(columnInfo.toString());
            s.append("\n");
        });
//        s.append("sql:\n");
//        s.append(selectAllSql).append("\n").append(insertAllColumnSql).append("\n").append(updateByIdSql).append("\n").append(deleteByIdSql);
        return s.toString();
    }


    public String getSelect() {
        return "SELECT * FROM " + tableName;
    }

    public String getInsert(boolean includeId) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder columnsb = new StringBuilder();
        StringBuilder placesb = new StringBuilder();
        columnList.stream().forEach(columnInfo -> {
            if (!(!includeId && columnInfo.getColumnName().equalsIgnoreCase("id"))) {
                columnsb.append(columnInfo.getColumnName()).append(",");
                placesb.append("?,");
            }
        });
        columnsb.deleteCharAt(columnsb.length() - 1);
        placesb.deleteCharAt(placesb.length() - 1);
        sb.append(columnsb);
        sb.append(")");
        sb.append(" VALUES (").append(placesb);
        sb.append(")");
        return sb.toString();
    }

    public String getUpdate() {
        StringBuilder sb = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        columnList.stream().forEach(columnInfo -> {
            if (!columnInfo.getColumnName().equalsIgnoreCase("id")) {
                sb.append(columnInfo.getColumnName()).append("=?");
                sb.append(",");
            }

        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" WHERE ").append(idRealName).append("=?");
        return sb.toString();
    }

    public String getDelete() {
        return "DELETE FROM " + tableName + " WHERE " + idRealName + "=?";
    }
}
