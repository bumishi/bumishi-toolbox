package org.bumishi.toolbox.metadata;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qiang.xie
 * @date 2016/9/20
 */
public class ColumnInfo {

    private String columnName;
    private String columnComment;
    private int sqlTypeIndex;
    private String sqlType;

    public ColumnInfo(String columnName, String columnComment, int sqlType) {
        this.columnName = columnName;
        this.columnComment = columnComment;
        this.sqlTypeIndex = sqlType;
        this.sqlType = getTypeName(sqlType);
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }


    public Class sqlTypeToJavaType() {
        if (sqlType == null || sqlType.trim().equals("") || sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("text")) {
            return String.class;
        }
        if (sqlType.equalsIgnoreCase("int") || sqlType.equalsIgnoreCase("smallint") || sqlType.equalsIgnoreCase("shorint") || sqlType.equalsIgnoreCase("tinyint") || sqlType.equalsIgnoreCase("mediumint")) {
            return Integer.class;
        }
        if (sqlType.equalsIgnoreCase("id") || sqlType.equalsIgnoreCase("integer")) {
            return Long.class;
        }

        if (sqlType.equalsIgnoreCase("float")) {
            return Float.class;
        }

        if (sqlType.equalsIgnoreCase("double")) {
            return Double.class;
        }

        if (sqlType.equalsIgnoreCase("DECIMAL")) {
            return BigDecimal.class;
        }

        if (sqlType.equalsIgnoreCase("blob")) {
            return byte[].class;
        }


        if (sqlType.equalsIgnoreCase("bit")) {
            return Boolean.class;
        }

        if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("TIMESTAMP")) {
            return Date.class;
        }
        return String.class;

    }

    public static String getTypeName(int sqlType) {
        switch (sqlType) {
            case -16:
                return "LONGNVARCHAR";
            case -15:
                return "NCHAR";
            case -9:
                return "NVARCHAR";
            case -8:
                return "ROWID";
            case -7:
                return "BIT";
            case -6:
                return "TINYINT";
            case -5:
                return "BIGINT";
            case -4:
                return "LONGVARBINARY";
            case -3:
                return "VARBINARY";
            case -2:
                return "BINARY";
            case 0:
                return "NULL";
            case 1:
                return "CHAR";
            case 2:
                return "NUMERIC";
            case 3:
                return "DECIMAL";
            case 4:
                return "INTEGER";
            case 5:
                return "SMALLINT";
            case 6:
                return "FLOAT";
            case 7:
                return "REAL";
            case 8:
                return "DOUBLE";
            case 12:
                return "VARCHAR";
            case 16:
                return "BOOLEAN";
            case 70:
                return "DATALINK";
            case 91:
                return "DATE";
            case 92:
                return "TIME";
            case 93:
                return "TIMESTAMP";
            case 2000:
                return "JAVA_OBJECT";
            case 2001:
                return "DISTINCT";
            case 2002:
                return "STRUCT";
            case 2003:
                return "ARRAY";
            case 2004:
                return "BLOB";
            case 2005:
                return "CLOB";
            case 2006:
                return "REF";
            case 2009:
                return "SQLXML";
            case 2011:
                return "NCLOB";
            default:
                return "OTHER";
        }
    }


    @Override
    public String toString() {
        return columnName + "," + sqlType + ",--" + columnComment;
    }
}
