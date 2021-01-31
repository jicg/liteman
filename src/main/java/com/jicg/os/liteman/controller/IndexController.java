package com.jicg.os.liteman.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jicg.os.liteman.gen.service.LmService;
import com.jicg.os.liteman.orm.repository.MenuRepository;
import com.jicg.os.liteman.orm.repository.TableRepository;
import com.jicg.os.liteman.orm.system.ColumnEntity;
import com.jicg.os.liteman.orm.system.ColumnType;
import com.jicg.os.liteman.orm.system.MenuEntity;
import com.jicg.os.liteman.orm.system.TableEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jicg on 2020/12/28
 */
@Slf4j
@RestController
public class IndexController {

    private final MenuRepository menuRepository;
    private final TableRepository tableRepository;
    private final ObjectMapper objectMapper;

    private final LmService lmService;

    public IndexController(MenuRepository menuRepository, TableRepository tableRepository, ObjectMapper objectMapper, LmService lmService) {
        this.menuRepository = menuRepository;
        this.tableRepository = tableRepository;
        this.objectMapper = objectMapper;
        this.lmService = lmService;
    }

    @GetMapping("/system")
    public List<MenuEntity> getIndex() {
        return menuRepository.findAll();
    }

    @GetMapping("/tables")
    public List<TableEntity> getTables() {
        return lmService.getTables();
    }

    @GetMapping("/menus")
    public List<MenuEntity> getMenus() {
        List<MenuEntity> menuEntityList = lmService.getMenus();
       // menuRepository.saveAll(menuEntityList);
        return menuEntityList;
    }

//    @GetMapping("/test")
//    public TableEntity test() throws JsonProcessingException {
//        TableEntity tableEntity = new TableEntity();
//        tableEntity.setName("table");
//        tableEntity.setLabel("表");
//        tableEntity.setDescription("");
//        tableEntity.setActive(true);
//        tableEntity.getColumnEntityList().
//                add(new ColumnEntity() {
//                    {
//                        this.setName("id");
//                        this.setLabel("id");
//                        this.setColumnType(ColumnType.Int);
//                    }
//                });
//        tableEntity.getColumnEntityList().add(new ColumnEntity() {
//            {
//                this.setName("name");
//                this.setLabel("数据库表名");
//                this.setColumnType(ColumnType.Date);
//            }
//        });
//        tableEntity.getColumnEntityList().add(new ColumnEntity() {
//            {
//                this.setName("nick_name");
//                this.setLabel("显示名");
//                this.setColumnType(ColumnType.Date);
//            }
//        });
//        tableEntity.getColumnEntityList().add(new ColumnEntity() {
//            {
//                this.setName("description");
//                this.setLabel("备注");
//                this.setColumnType(ColumnType.Date);
//            }
//        });
//        tableEntity.getColumnEntityList().add(new ColumnEntity() {
//            {
//                this.setName("col_type");
//                this.setLabel("类型");
//                this.setColumnType(ColumnType.String);
//            }
//        });
//        tableEntity.getColumnEntityList().add(new ColumnEntity() {
//            {
//                this.setName("u_date");
//                this.setColumnType(ColumnType.Date);
//                this.setDefVal("sysdate");
//            }
//        });
//        ColumnEntity columnEntity2 = new ColumnEntity();
//        tableEntity.getColumnEntityList().add(columnEntity2);
//        ColumnEntity columnEntity = new ColumnEntity();
//        columnEntity.setName("is_active");
//        columnEntity.setColumnType(ColumnType.Boolean);
//        columnEntity.setDefVal("true");
//        tableEntity.getColumnEntityList().add(columnEntity);
//
//        log.error(objectMapper.writeValueAsString(tableEntity));
//        return tableRepository.save(tableEntity);
//    }
//
//
//    @GetMapping("/test2")
//    public MenuEntity test2() throws JsonProcessingException {
//        MenuEntity menuEntity = new MenuEntity();
//        menuEntity.setActive(true);
//        menuEntity.setName("测试");
//        menuEntity.setIcon("");
//        menuEntity.setDescription("-----");
//        return menuRepository.save(menuEntity);
//    }
}
