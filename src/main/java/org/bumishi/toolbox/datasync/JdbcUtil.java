package org.bumishi.toolbox.datasync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiang.xie
 * @date 2016/9/20
 */
public abstract class JdbcUtil {

    private static Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    private static int batchSize = 1000;

    public static void setBatchSize(int batchSize) {
        JdbcUtil.batchSize = batchSize;
    }

    public static int executeUpdate(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        return executeUpdate(dataSource, sql, Arrays.asList(parameters));
    }

    public static int executeUpdate(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;
        Connection conn = null;
        int updateCount;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            updateCount = stmt.executeUpdate();
            logger.info("executeUpdate sql:{},param:{}", sql, parameters);
        } finally {
            close(stmt);
            close(conn);
        }

        return updateCount;
    }

    public static void executeBatchUpdate(DataSource dataSource, String sql, List<List<Object>> parameters) throws SQLException {
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);
            for (int index = 0; index < parameters.size(); index++) {
                List<Object> values = parameters.get(index);
                for (int i = 0; i < values.size(); i++) {
                    Object param = values.get(i);
                    stmt.setObject(i + 1, param);
                }
                stmt.addBatch();
                if (index + 1 % batchSize == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }

            }
            stmt.executeBatch();
            conn.commit();
            logger.info("executeUpdate sql:{}", sql);
        } catch (Exception e) {
            conn.rollback();
            logger.error("batchUpdate sql:{} error:{}", sql, e);
        } finally {
            close(stmt);
            close(conn);
        }


    }


    public static List<LinkedHashMap<String, Object>> executeQuery(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        return executeQuery(dataSource, sql, Arrays.asList(parameters));
    }

    public static List<LinkedHashMap<String, Object>> executeQuery(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        List<LinkedHashMap<String, Object>> rows = new ArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            rs = stmt.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();

            while (rs.next()) {
                LinkedHashMap row = new LinkedHashMap();
                int i = 0;

                for (int size = rsMeta.getColumnCount(); i < size; ++i) {
                    String columName = rsMeta.getColumnLabel(i + 1);
                    Object value = rs.getObject(i + 1);
                    row.put(columName, value);
                }

                rows.add(row);
            }
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

        return rows;
    }


    private static void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException {
        int i = 0;

        for (int size = parameters.size(); i < size; ++i) {
            Object param = parameters.get(i);
            stmt.setObject(i + 1, param);
        }

    }


    public static void close(Connection x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {

            }

        }
    }

    public static void close(Statement x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {

            }

        }
    }

    public static void close(ResultSet x) {
        if (x != null) {
            try {
                x.close();
            } catch (Exception var2) {

            }

        }
    }


}
