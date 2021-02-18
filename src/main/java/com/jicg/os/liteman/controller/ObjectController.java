package com.jicg.os.liteman.controller;

import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.json.JSONObject;
import com.jicg.os.liteman.gen.service.ObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jicg on 2021/2/16
 */
@Slf4j
@RestController
@RequestMapping("api")
public class ObjectController {

   private final ObjectService objectService;

    public ObjectController(ObjectService objectService) {
        this.objectService = objectService;
    }

    @GetMapping("/object/{tableName}/list")
    public List<Entity> list(@PathVariable String tableName) throws SQLException {
        objectService.getList(tableName).forEach(System.out::println);
        return objectService.getList(tableName);
    }

    @GetMapping("/object/{tableName}/get/{id}")
    public Entity get(@PathVariable String tableName, @PathVariable Long id) {
        return new Entity();
    }

    @GetMapping("/object/{tableName}/update/{id}")
    public Entity getIndex(@PathVariable String tableName, @PathVariable Long id) {
        return new Entity();
    }

    @GetMapping("/object/{tableName}/add")
    public Entity add(@PathVariable String tableName) {
        return new Entity();
    }

    @PostMapping("/object/{tableName}/test")
    public Entity test(@PathVariable String tableName, @RequestBody Entity entity) throws SQLException {
        System.out.println(entity);
        objectService.test(entity);
        return entity;
    }
}
