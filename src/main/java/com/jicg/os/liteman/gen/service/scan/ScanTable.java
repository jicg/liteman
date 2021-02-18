package com.jicg.os.liteman.gen.service.scan;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.jicg.os.liteman.gen.anno.*;
import com.jicg.os.liteman.gen.service.LmService;
import com.jicg.os.liteman.orm.repository.TableRepository;
import com.jicg.os.liteman.orm.system.*;
import com.jicg.os.liteman.utils.ScanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jicg on 2021/2/8
 */
@Slf4j
@Component
public class ScanTable {
    public final static Map<String, TableEntity> tableMap = new HashMap<>();
    public final static Map<String, ColumnData.Select> selectMap = new HashMap<>();
    private final TableRepository tableRepository;

    public ScanTable(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void scanPackages() throws Exception {
        doScanSelects().forEach(it -> {
            selectMap.put(it.getCode(), it);
        });
        doScanTables().forEach(it -> {
            tableMap.put(it.getName(), it);
        });
        tableRepository.findAll().forEach(it -> {
            tableMap.put(it.getName(), it);
        });
    }

    private List<ColumnData.Select> doScanSelects() throws ClassNotFoundException {
        List<ColumnData.Select> selectList = new ArrayList<>();
        for (String basePackage : LmService.basePackages) {
            for (BeanDefinition beanDefinition : ScanUtils.getScanBean(basePackage, LmColumnData.Select.class, LmColumnData.Selects.class)) {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                LmColumnData.Select[] selects = clazz.getAnnotationsByType(LmColumnData.Select.class);
                for (LmColumnData.Select select : selects) {
                    ColumnData.Select sel = getSelect(clazz, select);
                    selectList.add(sel);
                }
            }
        }
        return selectList;
    }

    private ColumnData.Select getSelect(Class<?> clazz, LmColumnData.Select select) {
        ColumnData.Select sel = new ColumnData.Select();
        sel.setCode(select.code());
        sel.setLabel(select.label());
        sel.setSelectOptions(Arrays.stream(select.options()).map(it -> {
            ColumnData.SelectOption selectOption = new ColumnData.SelectOption();
            selectOption.setLabel(it.label());
            selectOption.setValue(it.value());
            return selectOption;
        }).collect(Collectors.toSet()));
        return sel;
    }

    public List<TableEntity> doScanTables() throws Exception {
        List<TableEntity> tableEntities = new ArrayList<>();

        for (String basePackage : LmService.basePackages) {
            Set<BeanDefinition> beanDefinitions = ScanUtils.getScanBean(basePackage, LmTable.class);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

                TableEntity tableEntity = new TableEntity();
                LmTable lmTable = clazz.getAnnotation(LmTable.class);
                // 显示名称
                if (!lmTable.label().isEmpty()) tableEntity.setLabel(lmTable.label());
                else tableEntity.setLabel(clazz.getSimpleName());
                // 数据库name
                if (!lmTable.name().isEmpty()) tableEntity.setName(lmTable.name());
                else tableEntity.setName(StrUtil.toUnderlineCase(clazz.getSimpleName()));

//                System.out.println(lmTable.name()+"   "+StrUtil.toUnderlineCase(clazz.getSimpleName()));

                //字段列表
                Set<ColumnEntity> columnEntities = new HashSet<>();
                Arrays.stream(ReflectUtil.getFields(clazz)).forEach(field -> {
                    if (field.getAnnotation(LmIgnore.class) != null) {
                        return;
                    }
                    ColumnEntity columnEntity = new ColumnEntity();
                    LmColumn lmColumn = field.getAnnotation(LmColumn.class);
                    if (lmColumn != null) {
                        columnEntity.setLabel(lmColumn.label());
                        columnEntity.setName(lmColumn.name());
                        columnEntity.setDefVal(lmColumn.defVal());
                        columnEntity.setDescription(lmColumn.description());
                        columnEntity.setColumnType(lmColumn.columnType());
                    }
                    LmColumnData.Attr lmColumnAttr = field.getAnnotation(LmColumnData.Attr.class);
                    if (lmColumnAttr != null) {
                        ColumnData.ColumnAttr columnAttrEntity = new ColumnData.ColumnAttr();
                        columnAttrEntity.setCanEdit(lmColumnAttr.canEdit());
                        columnAttrEntity.setCanEditVisible(lmColumnAttr.canEditVisible());
                        columnAttrEntity.setCanExport(lmColumnAttr.canExport());
                        columnAttrEntity.setCanImport(lmColumnAttr.canImport());
                        columnAttrEntity.setCanListVisible(lmColumnAttr.canListVisible());
                        columnAttrEntity.setCanNew(lmColumnAttr.canNew());
                        columnAttrEntity.setCanNewVisible(lmColumnAttr.canNewVisible());
                        columnAttrEntity.setCanSearch(lmColumnAttr.canSearch());
                        columnEntity.setColumnAttrEntity(columnAttrEntity);
                    }
                    LmColumnData.SelectUse selectUse = field.getAnnotation(LmColumnData.SelectUse.class);
                    if (selectUse != null) {
                        ColumnData.SelectUse selUse = new ColumnData.SelectUse();
                        selUse.setColumnSelect(selectMap.get(selectUse.code()));
                        selUse.setColumnSelectDef(selectUse.def());
                        columnEntity.setSelectUse(selUse);
                    }

                    LmColumnData.Link lmColumnLink = field.getAnnotation(LmColumnData.Link.class);
                    if (lmColumnLink != null) {
                        ColumnData.ColumnLink columnLink = new ColumnData.ColumnLink();
                        columnLink.setTableName(StrUtil.toUnderlineCase(lmColumnLink.tableName()));
                        columnLink.setColumnName(StrUtil.toUnderlineCase(lmColumnLink.columnName()));
                        columnEntity.setColumnLink(columnLink);
                    }
                    if (columnEntity.getName() == null || columnEntity.getName().isEmpty()) {
                        columnEntity.setName(StrUtil.toUnderlineCase(field.getName()));
                    }
                    if (columnEntity.getLabel() == null || columnEntity.getLabel().isEmpty()) {
                        columnEntity.setLabel(field.getName());
                    }
                    if (columnEntity.getColumnType() == null || columnEntity.getColumnType() == ColumnData.Type.Auto) {
                        columnEntity.setColumnType(ColumnData.Type.Auto);
                    }
                    columnEntity.setActive(true);
                    columnEntities.add(columnEntity);
                });
                tableEntity.setColumnEntityList(columnEntities);
                tableEntity.setActive(true);
                tableEntities.add(tableEntity);
            }

        }
        return tableEntities;
    }


    public TableEntity getTable(String table) {
        return tableMap.get(table);
    }

    public List<TableEntity> getTables() {
        List<TableEntity> tableEntities = new ArrayList<>();
        tableMap.forEach((key, table) -> {
            tableEntities.add(table);
        });
        return tableEntities;
    }
}
