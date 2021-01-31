package com.jicg.os.liteman.gen.service;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jicg.os.liteman.gen.anno.*;
import com.jicg.os.liteman.gen.service.scan.ScanMenu;
import com.jicg.os.liteman.orm.repository.MenuRepository;
import com.jicg.os.liteman.orm.repository.TableRepository;
import com.jicg.os.liteman.orm.system.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jicg on 2021/1/3
 */
@Slf4j
@Service
public class LmService implements ApplicationRunner {
    //region init
    public static List<String> basePackages = new ArrayList<String>();
    public final static Map<String, TableEntity> tableMap = new HashMap<>();
    public final static List<MenuEntity> MENU_ENTITIES = new ArrayList<>();

    final ScanMenu scanMenu;
    final ObjectMapper objectMapper;
    final TableRepository tableRepository;
    final MenuRepository menuRepository;

    public LmService(ScanMenu scanMenu, ObjectMapper objectMapper,
                     TableRepository tableRepository, MenuRepository menuRepository) {
        this.scanMenu = scanMenu;
        this.objectMapper = objectMapper;
        this.tableRepository = tableRepository;
        this.menuRepository = menuRepository;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MenuEntity> menusQ = menuRepository.findAllByCodeIsNull();
        scanMenu.scanMenuPackages(menusQ);


//        MENU_ENTITIES.removeIf(it -> menusQ.stream().anyMatch(q -> q.getCode().equals(it.getCode())));
//        MENU_ENTITIES.addAll(menuRepository.findAllByMenuIdIsNull());
//        log.info("menus: " + objectMapper.writeValueAsString(MENU_ENTITIES));
        scanPackages().forEach(it -> {
            tableMap.put(it.getName(), it);
        });
        tableRepository.findAll().forEach(it -> {
            tableMap.put(it.getName(), it);
        });
    }


    public List<TableEntity> scanPackages() throws Exception {
        List<TableEntity> tableEntities = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false);
        for (String basePackage : basePackages) {
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
                        ColumnAttrEntity columnAttrEntity = new ColumnAttrEntity();
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
                        ColumnLinkEntity columnLink = new ColumnLinkEntity();
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

    public List<MenuEntity> getMenus() {
        return MENU_ENTITIES;
    }

//    public static void findClassAllFields(Class<?> clazz, Consumer<Field> fieldConsumer) throws Exception {
//
//        Class<?> tempClass = clazz;
//        while (null != tempClass) {
//            for (Field field : tempClass.getDeclaredFields()) {
//                int mod = field.getModifiers();
//                if (Modifier.isStatic(mod) || Modifier.isInterface(mod)) {
//                    continue;
//                }
//                fieldConsumer.accept(field);
//            }
//            tempClass = tempClass.getSuperclass();
//        }
//    }

    //endregion
}
