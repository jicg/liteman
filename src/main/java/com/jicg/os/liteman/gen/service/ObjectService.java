package com.jicg.os.liteman.gen.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import com.jicg.os.liteman.orm.system.ColumnData;
import com.jicg.os.liteman.orm.system.ColumnEntity;
import com.jicg.os.liteman.orm.system.TableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jicg on 2021/2/16
 */
@Slf4j
@Service
public class ObjectService {

    private final DataSource dataSource;

    private final EntityManager entityManager;

    private final LmService lmService;


    public ObjectService(LmService lmService, DataSource dataSource, EntityManager entityManager) {
        this.lmService = lmService;
        this.dataSource = dataSource;
        this.entityManager = entityManager;
    }

    public List<Entity> getData() throws SQLException {
        return DbUtil.use(dataSource).query("select * from table_entity");
    }

    public void test(Entity entity) throws SQLException {
        entity.setTableName("table_entity");
        log.error(entity.toString());

        DbUtil.use(dataSource).insert(entity);
    }


    public List<Entity> getList(String tableName) throws SQLException {
        List<Entity> arrayList = new ArrayList<Entity>();
        TableEntity tableEntity = lmService.getTable(tableName);
        if (tableEntity == null) return arrayList;
        SqlTable sqlTable = new SqlTable(tableEntity);
        init(sqlTable);
        toSql(sqlTable);
        return arrayList;
    }

    private void toSql(SqlTable sqlTable) {
//        TemplateUtil.createEngine().getTemplate("data/convert.ftl")
//                .render(
//                        MapUtil.builder(new HashMap<String, Object>())
//                                .put("table", sqlTable).build());
        StringBuffer ss = new StringBuffer("select ");
        StringBuffer ts = new StringBuffer("from  ");

        Map<String, String> alias = new HashMap<>();
        AtomicInteger index = new AtomicInteger(1);
        ts.append(sqlTable.getTable().getName()).append(" t ");
        sqlTable.sqlTableLinks.forEach((s, sqlTableLink) -> {
            String[] strs = getAllSplitStr(s);
            for (int i = 0; i < strs.length; i++) {
                String str = strs[i];
                if (!alias.containsKey(str)) {
                    SqlTableLink tableLink = sqlTable.sqlTableLinks.get(str);
                    String talias = "t" + index.getAndIncrement();
                    String upStr = str.contains(";") ? str.substring(0, str.lastIndexOf(";")) : "";
                    String upAlias = StrUtil.isEmpty(upStr) ? " t" : alias.get(upStr);
                    ts.append(" left join ")
                            .append(tableLink.getLinkTable().getName()).append(" ").append(talias)
                            .append(" on ( ").append(talias).append(".id = ").append(upAlias).append(".").append(tableLink.getMainColumn().getName()).append(") ");
                    alias.put(str, talias);
                }
            }

        });
        log.error(ts.toString());
    }


    public void init(SqlTable sqlTable) {
        for (ColumnEntity column : sqlTable.table.getColumnEntityList()) {
            SqlColumnInfo sqlColumnInfo = new SqlColumnInfo(sqlTable, column);
            String[] keys = sqlColumnInfo.getColumnLinkNames();
            if (keys.length > 0) {
                TableEntity mainTable = sqlTable.table;
                ColumnEntity mainColumn = column;
                TableEntity linkTable = null;
                for (String key : keys) {
                    if (sqlTable.sqlTableLinks.containsKey(key)){
                         mainTable = sqlTable.sqlTableLinks.get(key).mainTable;
                         mainColumn = sqlTable.sqlTableLinks.get(key).mainColumn;
                        continue;
                    }
                    String lastTableName = key.contains(";") ? key.substring(key.lastIndexOf(";") + 1) : key;

                    Optional<ColumnEntity> linkColumnOpt = mainTable.getColumnEntityList().stream().filter(c -> c.getName().equals(lastTableName)).findFirst();
                    if (!linkColumnOpt.isPresent())
                        throw new RuntimeException("关联失败：表：" + mainTable.getName() + " 字段：" + mainColumn.getName() + ", 没法关联 。" + lastTableName);
                    mainColumn = linkColumnOpt.get();
                    ColumnData.ColumnLink columnLink = mainColumn.getColumnLink();
                    if (columnLink == null)
                        throw new RuntimeException("关联失败：表：" + mainTable.getName() + " 字段：" + mainColumn.getName() + ", 没法关联 。"
                                + "字段" + mainColumn.getName() + " 未定义关联关系");

                    linkTable = lmService.getTable(columnLink.getTableName());
                    if (linkTable == null) {
                        throw new RuntimeException("关联失败：表：" + sqlTable.table.getName() + " 字段：" + mainColumn.getName() + ", 没法关联 。"
                                + " 关联表 ：" + columnLink.getTableName() + " 未定义");
                    }
                    sqlTable.sqlTableLinks.put(key, new SqlTableLink(mainTable, mainColumn, linkTable));
                    mainTable = linkTable;
                }
            }
            sqlTable.columnInfos.add(sqlColumnInfo);
        }
    }

    @NoArgsConstructor
    @Data
    public static class SqlTable {

        TableEntity table;

        public SqlTable(TableEntity table) {
            this.table = table;
        }

        private List<SqlColumnInfo> columnInfos = new ArrayList<>();
        private List<WhereColumnInfo> whereColumnInfos = new ArrayList<>();
        private Map<String, SqlTableLink> sqlTableLinks = new HashMap<>();
    }


    @NoArgsConstructor
    @Data
    public static class SqlTableLink {
        private TableEntity mainTable;
        private ColumnEntity mainColumn;
        private TableEntity linkTable;


        public SqlTableLink(TableEntity mainTable, ColumnEntity mainColumn, TableEntity linkTable) {
            this.mainTable = mainTable;
            this.mainColumn = mainColumn;
            this.linkTable = linkTable;
        }
    }

    @NoArgsConstructor
    @Data
    public static class SqlColumnInfo {
        private SqlTable table;
        private ColumnEntity column;

        public SqlColumnInfo(SqlTable table, ColumnEntity column) {
            this.table = table;
            this.column = column;
        }

        //        private TableEntity table;
//        private ColumnEntity column;
//
//        public SqlColumnInfo(SqlTable table, ColumnEntity column) {
//            this.table = table.table;
//            this.column = column;
//        }
        public String getColumnLinkName() {
            int index = column.getName().lastIndexOf(";");
            if (index <= 0) return "";
            return column.getName().substring(0, index);
        }

        public String getColumnName() {
            int index = column.getName().lastIndexOf(";");
            if (index <= 0) return column.getName();
            return column.getName().substring(index + 1);
        }

        public String[] getColumnLinkNames() {
            if (column.getColumnLink() != null) {
                return new String[]{column.getName()};
            }
            if (column.getName() == null || !column.getName().contains(";")) return new String[]{};
            StringBuilder str = new StringBuilder();
            String[] cols = column.getName().split(";");
            String[] strs = new String[cols.length - 1];
            for (int i = 0; i < cols.length - 1; i++) {
                str.append(cols[i]);
                strs[i] = str.toString();
                str.append(";");
            }
            return strs;
        }

    }

    @Data
    public static class WhereColumnInfo {
        private SqlTable table;
        private ColumnEntity column;
        private Object value;

        public String getColumnLinkName() {
            int index = column.getName().lastIndexOf(";");
            if (index <= 0) return "";
            return column.getName().substring(0, index);
        }

        public String getColumnName() {
            int index = column.getName().lastIndexOf(";");
            if (index <= 0) return column.getName();
            return column.getName().substring(index + 1);
        }
    }


    public static String[] getAllSplitStr(String s) {
        StringBuilder str = new StringBuilder();
        String[] cols = s.split(";");
        String[] strs = new String[cols.length];
        for (int i = 0; i < cols.length; i++) {
            str.append(cols[i]);
            strs[i] = str.toString();
            str.append(";");
        }
        return strs;
    }

}
