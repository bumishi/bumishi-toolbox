##日常用工具##

**目前已有功能包简介**

- metadata
    - 基于jdbc获取数据库表的元数据信息
    ```

    public static void main(String[] arg) {
        DataSource dataSource= DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver").url("jdbc:mysql://localhost/kugou_show?autoReconnect=true&characterEncoding=utf-8").username("root").password("root").build();
        JdbcMetaDataFetcher fetcher=new JdbcMetaDataFetcher(dataSource);
        System.out.println(fetcher.getAllTables());
    }

    ```
- datasync
    - 基于jdbc实现两个数据源之间指定表的数据同步
    ```
     public static void main(String[] arg) throws Exception{
            DataSource source= DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver").url("jdbc:mysql://localhost/b?autoReconnect=true&characterEncoding=utf-8").username("root").password("root").build();

            DataSource dest= DataSourceBuilder.create().driverClassName("com.mysql.jdbc.Driver").url("jdbc:mysql://localhost/a?autoReconnect=true&characterEncoding=utf-8").username("root").password("root").build();
            TableDataSynchronizer tableDataSync=new TableDataSynchronizer(source,dest);
            tableDataSync.syncTable("init_config");//以id为主键
            tableDataSync.syncTable("switch_config", Lists.newArrayList("cons_key","id2"));//复合主键
        }
    ```


###交流群:245130488###


##专注Java技术研究的公众号:javajidi_com##


