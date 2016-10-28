package org.bumishi.toolbox.datasync;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiang.xie
 * @date 2016/9/20
 * 批量sql插入
 */
public class SqlInsert {

    private String table;

    private List<LinkedHashMap<String, Object>> datarows;//带插入的数据行，包含列信息

    private String sql;//insert sql

    private List<List<Object>> paramsValues = new ArrayList<>();//参数值

    public SqlInsert(String table, List<LinkedHashMap<String, Object>> datarows) {
        if (table == null || table.trim().equals("") || datarows == null || datarows.isEmpty()) {
            throw new IllegalArgumentException("非法数据");
        }
        this.table = table;
        this.datarows = datarows;
        buildInsertSql(datarows.get(0));
        buildInsertValues();
    }

    private void buildInsertSql(LinkedHashMap<String, Object> row) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append("`" + table + "`").append(" (");
        StringBuilder place = new StringBuilder();
        int i = 1;
        for (String key : row.keySet()) {
            sb.append("`" + key + "`");
            place.append("?");
            if (i < row.size()) {
                sb.append(",");
                place.append(",");
            }
            i++;
        }
        sb.append(")").append(" VALUES (").append(place).append(")");
        sql = sb.toString();
    }

    private void buildInsertValues() {
        datarows.stream().forEach(row -> {
            List onerowData = new ArrayList<>();
            row.entrySet().stream().forEach(entry -> {
                onerowData.add(entry.getValue());
            });
            paramsValues.add(onerowData);
        });
    }

    public String getSql() {
        return sql;
    }

    public List<List<Object>> getParamsValues() {
        return paramsValues;
    }
}
