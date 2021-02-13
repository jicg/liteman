package com.jicg.os.liteman.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jicg.os.liteman.gen.service.LmService;
import com.jicg.os.liteman.orm.repository.MenuRepository;
import com.jicg.os.liteman.orm.repository.TableRepository;
import com.jicg.os.liteman.orm.system.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jicg on 2020/12/28
 */
@Slf4j
@RestController
@RequestMapping("api")
public class IndexController {


    private final LmService lmService;

    public IndexController(LmService lmService) {
        this.lmService = lmService;
    }

    @GetMapping("/sys/systems")
    public List<SubSystemEntity> getIndex() {
        return lmService.getSubSystems();
    }

    @GetMapping("/sys/table/{tableCode}")
    public TableEntity getTables(@PathVariable String tableCode) {
        TableEntity dataTable = lmService.getTable(tableCode);
        if (dataTable == null) throw new RuntimeException("表不存在！");
        return dataTable;
    }

    @GetMapping("/sys/menus")
    public List<MenuEntity> getMenus(String subSystemCode) {
        return lmService.getMenus(subSystemCode);
    }

    @GetMapping("/sys/menu/{menuCode}")
    public MenuEntity getMenu(@PathVariable String menuCode) {
        return lmService.getMenu(menuCode);
    }
}
