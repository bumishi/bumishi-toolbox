package org.bumishi.toolbox.datasync;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiang.xie
 * @date 2016/9/20
 * 两个数据源之间相同结构的数据表数据同步器
 * 同步策略：原表中不在目标表中的数据将新增到目标表中，目标表中不在原表中的数据会被删除，两个表中都存在的数据，以原本为标准更新到目标表
 */
public class TableDataSynchronizer {

    private DataSource source;

    private DataSource dest;


    public TableDataSynchronizer(DataSource source, DataSource dest) {
        this.source = source;
        this.dest = dest;
    }

    /***
     * 将指定的表从源数据库同步到目标数据库
     *
     * @param tableName 表名
     * @param pkFields  主键字段，或者能作为唯一标识一条数据的字段，支持复合主键
     * @throws Exception
     */
    public void syncTable(String tableName, List<String> pkFields) throws Exception {

        String sql = "select * from `" + tableName + "`";
        List<LinkedHashMap<String, Object>> sourceList = JdbcUtil.executeQuery(source, sql);
        if (sourceList == null || sourceList.isEmpty()) {
            return;
        }
        List<LinkedHashMap<String, Object>> destList = JdbcUtil.executeQuery(dest, sql);

        MapCollectionDiff collectionDiff = new MapCollectionDiff(sourceList, destList, pkFields);
        collectionDiff.analysisDiff();
        Collection deletes = collectionDiff.getRemoves();
        Collection updates = collectionDiff.getUpdates();
        Collection news = collectionDiff.getNews();

        if (deletes != null && !deletes.isEmpty()) {
            SqlDelete sqlDelete = new SqlDelete(tableName, (List<LinkedHashMap<String, Object>>) deletes, pkFields);
            JdbcUtil.executeBatchUpdate(dest, sqlDelete.getSql(), sqlDelete.getParamsValues());
        }

        if (updates != null && !updates.isEmpty()) {
            SqlUpdate sqlUpdate = new SqlUpdate(tableName, (List<LinkedHashMap<String, Object>>) updates, pkFields);
            JdbcUtil.executeBatchUpdate(dest, sqlUpdate.getSql(), sqlUpdate.getParamsValues());
        }

        if (news != null && !news.isEmpty()) {
            SqlInsert sqlInsert = new SqlInsert(tableName, (List<LinkedHashMap<String, Object>>) news);
            JdbcUtil.executeBatchUpdate(dest, sqlInsert.getSql(), sqlInsert.getParamsValues());
        }
    }

    /***
     * 将指定的表从源数据库同步到目标数据库，主键字段名为id
     *
     * @param tableName 表名
     * @throws Exception
     * @see TableDataSynchronizer#syncTable(String, List)
     */
    public void syncTable(String tableName) throws Exception {
        syncTable(tableName, Collections.singletonList("id"));
    }

}
