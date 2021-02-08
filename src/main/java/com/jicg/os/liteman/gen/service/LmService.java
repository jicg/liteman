package com.jicg.os.liteman.gen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jicg.os.liteman.gen.service.scan.ScanMenu;
import com.jicg.os.liteman.gen.service.scan.ScanTable;
import com.jicg.os.liteman.orm.repository.MenuRepository;
import com.jicg.os.liteman.orm.repository.TableRepository;
import com.jicg.os.liteman.orm.system.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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

    final ScanMenu scanMenu;
    final ScanTable scanTable;
    final ObjectMapper objectMapper;

    public LmService(ScanMenu scanMenu, ScanTable scanTable, ObjectMapper objectMapper) {
        this.scanMenu = scanMenu;
        this.scanTable = scanTable;
        this.objectMapper = objectMapper;
    }

    public TableEntity getTable(String table) {
        return scanTable.getTable(table);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        scanMenu.scanMenuPackages();
        scanTable.scanPackages();
    }

    public List<MenuEntity> getMenus(String subSystemCode) {
        return scanMenu.subSystemCache.get(subSystemCode);
    }

    public List<SubSystemEntity> getSubSystems() {
        return scanMenu.subSystems;
    }
}
