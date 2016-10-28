package org.bumishi.toolbox.datasync;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiang.xie
 * @date 2016/9/20
 */
public class SqlDelete {

    private String table;

    private List<LinkedHashMap<String, Object>> datarows;//待删除的数据行，包含列信息

    private String sql;//delete sql

    private List<List<Object>> paramsValues = new ArrayList<>();//参数值

    private List<String> pkFileds;//主键字段


    public SqlDelete(String table, List<LinkedHashMap<String, Object>> datarows, List<String> pkFileds) {
        if (table == null || table.trim().equals("") || datarows == null || datarows.isEmpty()) {
            throw new IllegalArgumentException("非法数据");
        }
        this.table = table;
        this.datarows = datarows;
        this.pkFileds = pkFileds;
        buildDeleteSql(datarows.get(0));
        buildDeleteValues();
    }

    private void buildDeleteSql(LinkedHashMap<String, Object> row) {
        StringBuilder sb = new StringBuilder("DELETE FROM ").append("`" + table + "`").append(" WHERE ");
        for (int i = 0; i < pkFileds.size(); i++) {
            sb.append("`" + pkFileds.get(i) + "`").append("=?");
            if (i < pkFileds.size() - 1) {
                sb.append(" AND ");
            }
        }
        sql = sb.toString();
    }

    private void buildDeleteValues() {
        datarows.stream().forEach(row -> {
            List onerowData = new ArrayList<>();
            row.entrySet().stream().forEach(entry -> {
                if (pkFileds.contains(entry.getKey())) {
                    onerowData.add(entry.getValue());
                }
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
