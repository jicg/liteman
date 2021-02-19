package com.jicg.os.liteman.gen.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONUtil;
import com.jicg.os.liteman.orm.system.ColumnData;
import com.jicg.os.liteman.orm.system.ColumnEntity;
import com.jicg.os.liteman.orm.system.TableEntity;
import com.jicg.os.liteman.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author jicg on 2021/2/16
 */
@Slf4j
@Service
public class ObjectService {

    private final DataSource dataSource;

    private final LmService lmService;


    public ObjectService(LmService lmService, DataSource dataSource) {
        this.lmService = lmService;
        this.dataSource = dataSource;
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
        SqlTable sqlTable = new SqlTable(tableEntity, lmService::getTable);
        sqlTable.toSql();
        return arrayList;
    }


    @Data
    public static class SqlTable {

        TableEntity table;
        Function<String, TableEntity> func;

        public SqlTable(TableEntity table, Function<String, TableEntity> func) {
            this.table = table;
            this.func = func;
            init();
        }

        public void init() {
            for (ColumnEntity column : table.getColumnEntityList()) {
                String str = StringUtils.commaRemoveLast(column.getName());
                if (column.getColumnLink() != null) {
                    str = column.getName();
                }
                String[] keys = StringUtils.commaSplit(str);
                for (int i = 0; i < keys.length; i++) {
                    String key = keys[i];
                    if (StrUtil.isNotEmpty(key) && !linkColumnNames.contains(key)) {
                        linkColumnNames.add(key);
                    }
                }

            }
            linkColumnNames.sort(String::compareTo);


            for (ColumnEntity column : table.getColumnEntityList()) {
                SqlColumnInfo sqlColumnInfo = new SqlColumnInfo(this, column);
                columnInfos.add(sqlColumnInfo);
                if (column.getColumnLink() != null) {
                    ColumnData.ColumnLink columnLink = column.getColumnLink();
                    TableEntity linkTable = func.apply(columnLink.getTableName());
                    if (linkTable == null) {
                        throw new RuntimeException("关联失败：表：" + table.getName() + " 字段：" + column.getName() + ", 没法关联 。"
                                + " 关联表 ：" + columnLink.getTableName() + " 未定义");
                    }
                    ColumnEntity linkColumn = linkTable.getColumnEntityList().stream().filter(c -> c.getName().equalsIgnoreCase(columnLink.getColumnName())).findFirst().orElse(null);
                    sqlTableLinks.put(column.getName(), new SqlTableLink(table, column, linkTable, linkColumn));
                }
            }

//        List<String> linkColumnNames = (columnNames == null || columnNames.size() == 0) ? sqlTable.linkColumnNames : columnNames;
            for (String key : linkColumnNames) {
                if (sqlTableLinks.containsKey(key)) continue;
                SqlTableLink sqlTableLinkUp = sqlTableLinks.get(StringUtils.commaRemoveLast(key));
                String lastTableName = StringUtils.commaLast(key);

                TableEntity mainTable = sqlTableLinkUp.linkTable;
                ColumnEntity mainColumn = mainTable.getColumnEntityList().stream().filter(c -> c.getName().equalsIgnoreCase(lastTableName)).findFirst().orElseThrow(
                        () -> new RuntimeException("关联失败：表：" + mainTable.getName() + " 字段：" + lastTableName + " 不存在！ 。")
                );
                ColumnData.ColumnLink columnLink = mainColumn.getColumnLink();
                if (columnLink == null)
                    throw new RuntimeException("关联失败：表：" + mainTable.getName() + " 字段：" + mainColumn.getName() + ", 没法关联 。"
                            + "字段" + mainColumn.getName() + " 未定义关联关系");

                TableEntity linkTable = func.apply(columnLink.getTableName());
                if (linkTable == null) {
                    throw new RuntimeException("关联失败：表：" + table.getName() + " 字段：" + mainColumn.getName() + ", 没法关联 。"
                            + " 关联表 ：" + columnLink.getTableName() + " 未定义 ");
                }
                ColumnEntity linkColumn = linkTable.getColumnEntityList().stream().filter(c -> c.getName().equalsIgnoreCase(columnLink.getColumnName())).findFirst().orElse(null);
                sqlTableLinks.put(key, new SqlTableLink(mainTable, mainColumn, linkTable, linkColumn));
            }
        }

        private List<SqlColumnInfo> columnInfos = new ArrayList<>();
        private List<WhereColumnInfo> whereColumnInfos = new ArrayList<>();
        private List<String> linkColumnNames = new ArrayList<>();
        private Map<String, SqlTableLink> sqlTableLinks = new HashMap<>();


        private void toSql() {
            SqlTable sqlTable = this;
            StringBuffer ss = new StringBuffer("select ");
            StringBuffer ts = new StringBuffer("from  ");
            Map<String, String> alias = new HashMap<>();
            AtomicInteger index = new AtomicInteger(1);
            ts.append(sqlTable.getTable().getName()).append(" t ");
            sqlTable.linkColumnNames.forEach((str) -> {
                if (!alias.containsKey(str)) {
                    SqlTableLink tableLink = sqlTable.sqlTableLinks.get(str);
                    String talias = "t" + index.getAndIncrement();
                    String upStr = StringUtils.commaRemoveLast(str);
                    String upAlias = StrUtil.isEmpty(upStr) ? " t" : alias.get(upStr);
                    ts.append(" left join ")
                            .append(tableLink.getLinkTable().getName()).append(" ").append(talias)
                            .append(" on ( ").append(talias).append(".").append(tableLink.linkColumn == null ? "id" : tableLink.linkColumn.getName()).append(" = ").append(upAlias).append(".").append(tableLink.getMainColumn().getName()).append(") ");
                    alias.put(str, talias);
                }
            });
            log.error(ts.toString());
        }

    }


    @NoArgsConstructor
    @Data
    public static class SqlTableLink {
        private TableEntity mainTable;
        private ColumnEntity mainColumn;
        private TableEntity linkTable;
        private ColumnEntity linkColumn;


        public SqlTableLink(TableEntity mainTable, ColumnEntity mainColumn, TableEntity linkTable, ColumnEntity linkColumn) {
            this.mainTable = mainTable;
            this.mainColumn = mainColumn;
            this.linkTable = linkTable;
            this.linkColumn = linkColumn;
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

        public String getColumnLinkName() {
            return StringUtils.commaRemoveLast(column.getName());
        }

        public String getColumnName() {
            return StringUtils.commaLast(column.getName());
        }

        public String[] getColumnLinkNames() {
            if (column.getColumnLink() != null) {
                return new String[]{column.getName()};
            }
            if (column.getName() == null || !column.getName().contains(";")) return new String[]{};
            return StringUtils.commaSplit(StringUtils.commaRemoveLast(column.getName()));
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


}
