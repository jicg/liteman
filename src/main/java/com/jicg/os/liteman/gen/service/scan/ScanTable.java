package com.jicg.os.liteman.gen.service.scan;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.jicg.os.liteman.gen.anno.*;
import com.jicg.os.liteman.gen.service.LmService;
import com.jicg.os.liteman.orm.repository.TableRepository;
import com.jicg.os.liteman.orm.system.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author jicg on 2021/2/8
 */
@Slf4j
@Component
public class ScanTable {
    public final static Map<String, TableEntity> tableMap = new HashMap<>();
    private final TableRepository tableRepository;

    public ScanTable(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void scanPackages() throws Exception {
        doScanPackages().forEach(it -> {
            tableMap.put(it.getName(), it);
        });
        tableRepository.findAll().forEach(it -> {
            tableMap.put(it.getName(), it);
        });
    }


    public List<TableEntity> doScanPackages() throws Exception {
        List<TableEntity> tableEntities = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false);
        for (String basePackage : LmService.basePackages) {
            scanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(LmTable.class));
            Set<BeanDefinition> beanDefinitions = scanningCandidateComponentProvider.findCandidateComponents(basePackage);
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
                    LmColumnAttr lmColumnAttr = field.getAnnotation(LmColumnAttr.class);
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
                    LmColumnLink lmColumnLink = field.getAnnotation(LmColumnLink.class);
                    if (field.getAnnotation(LmColumnLink.class) != null) {
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
                    if (columnEntity.getColumnType() == null || columnEntity.getColumnType() == ColumnType.Auto) {
                        columnEntity.setColumnType(ColumnType.Auto);
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
