package com.jicg.os.liteman.gen.service.scan;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jicg.os.liteman.gen.anno.*;
import com.jicg.os.liteman.gen.service.LmService;
import com.jicg.os.liteman.orm.repository.MenuRepository;
import com.jicg.os.liteman.orm.system.MenuEntity;
import com.jicg.os.liteman.orm.system.SubSystemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.sun.xml.internal.xsom.impl.UName.comparator;

/**
 * @author jicg on 2021/1/13
 */
@Slf4j
@Component
public class ScanMenu {
    public Map<String, MenuEntity> cacheMenus = new HashMap<>();
    //    public List<MenuEntity> menus = new ArrayList<>();
    public List<SubSystemEntity> subSystems = new ArrayList<>();
    public Map<String, List<MenuEntity>> subSystemCache = new HashMap<String, List<MenuEntity>>();

    final MenuRepository menuRepository;

    public ScanMenu(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void scanMenuPackages() throws ClassNotFoundException {
        Map<String, MenuEntity> cache = new HashMap<>();
        List<MenuEntity> menuEntities = new ArrayList<>();

        //获取数据库中的 菜单
        List<MenuEntity> scanMenuEntities = new ArrayList<>();
        List<SubSystemEntity> scanSubSystemEntities = new ArrayList<>();
        for (String basePackage : LmService.basePackages) {

            for (BeanDefinition beanDefinition : getScanBean(basePackage, LmSubSystem.class)) {
                Class<?> beanClazz = Class.forName(beanDefinition.getBeanClassName());
                LmSubSystem lmSubSystem = beanClazz.getAnnotation(LmSubSystem.class);
                scanSubSystemEntities.add(getSubSystem(lmSubSystem, beanClazz));
            }
            for (BeanDefinition beanDefinition : getScanBean(basePackage, LmMenuDirs.class, LmMenuDir.class, LmMenus.class, LmMenu.class)) {
                Class<?> beanClazz = Class.forName(beanDefinition.getBeanClassName());
                List<MenuEntity> menus = new ArrayList<>();
                LmMenuDir[] lmMenuDirs = beanClazz.getAnnotationsByType(LmMenuDir.class);
                for (LmMenuDir lmMenuDir : lmMenuDirs) {
                    menus.addAll(getMenuDirEntity(lmMenuDir, beanClazz));
                }
                LmMenu[] lmMenus = beanClazz.getAnnotationsByType(LmMenu.class);
                for (LmMenu lmMenu : lmMenus) {
                    MenuEntity menu = getMenuEntity(lmMenu, beanClazz);
                    menus.add(menu);

                }

                LmSubSystem lmSubSystem = beanClazz.getAnnotation(LmSubSystem.class);
                if (lmSubSystem != null) {
                    menus.stream().filter(menuEntity -> StrUtil.isEmpty(menuEntity.getSystemCode())).forEach(menu -> {
                        menu.setSystemCode(lmSubSystem.code());
                    });
                }
                scanMenuEntities.addAll(menus);
            }
            scanMenuEntities.sort((o1, o2) -> (int) (o1.getSort() - o2.getSort()));
        }
        scanMenuEntities.forEach(menuEntity -> {
            if (cache.containsKey(menuEntity.getCode())
                    && menuEntity.getZIndex() <= cache.get(menuEntity.getCode()).getZIndex()) {
                return;
            }
            cache.put(menuEntity.getCode(), menuEntity);
            menuEntities.add(menuEntity);
        });
        //获取数据库中的 菜单
        List<MenuEntity> dbMenuEntities = menuRepository.findAll();
        dbMenuEntities.sort((o1, o2) -> (int) (o1.getSort() - o2.getSort()));
        dbMenuEntities.forEach(menuEntity -> {
            if (cache.containsKey(menuEntity.getCode())
                    && menuEntity.getZIndex() <= cache.get(menuEntity.getCode()).getZIndex()) {
                return;
            }
            cache.put(menuEntity.getCode(), menuEntity);
            menuEntities.add(menuEntity);
        });
        //去重复
        subSystems = scanSubSystemEntities.stream().sorted((o1, o2) -> (int) (o2.getZIndex() - o1.getZIndex())).collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(
                                () -> new TreeSet<>((o1, o2) -> o2.getCode().compareTo(o1.getCode()))
                        ), ArrayList::new
                ));
        subSystemCache = toTreeMenu(cache, menuEntities);
        cacheMenus = cache;


////
////        Map<String, MenuEntity> idMenus = new HashMap<>();
////        menuEntities.forEach(menuEntity -> {
////            idMenus.put(menuEntity.getUpCode(), menuEntity);
////        });
////        menuEntities.forEach(menuEntity -> {
////            if (StrUtil.isEmpty(menuEntity.getUpCode())) return;
////            idMenus.get(menuEntity.getUpCode()).getChildList().add(menuEntity);
////        });
////        List<String> notTops = new ArrayList<>();
////        cache.forEach((code, menu) -> {
////            log.info(code + "   " + menu.getUpCode());
////            if (cache.containsKey(menu.getUpCode())) {
////                MenuEntity itemUp = cache.get(menu.getUpCode());
////                itemUp.getChildList().add(menu);
////            }
////            if (!menu.getUpCode().equals("object")) {
////                notTops.add(code);
////            }
////        });
////
////
////
//        notTops.forEach(str -> {
//            cache.remove(str);
//        });
//        cache.forEach((code, menu) -> {
//            LmService.MENU_ENTITIES.add(menu);
//        });
//        sort(LmService.MENU_ENTITIES);

    }


    private Map<String, List<MenuEntity>> toTreeMenu(Map<String, MenuEntity> cache, List<MenuEntity> menuEntities) {
        if (cache == null || menuEntities == null) return new HashMap<>();
        menuEntities.stream().filter(it -> StrUtil.isNotEmpty(it.getUpCode())).forEach(menu -> {
            if (cache.containsKey(menu.getUpCode())) {
                cache.get(menu.getUpCode()).getChildList().add(menu);
            }
        });
        return menuEntities.stream().filter(
                it -> StrUtil.isNotEmpty(it.getSystemCode()) && StrUtil.isEmpty(it.getUpCode())
        ).collect(Collectors.groupingBy(MenuEntity::getSystemCode));
    }

    public Set<BeanDefinition> getScanBean(String basePackage, Class<? extends Annotation>... annotationTypes) {
        ClassPathScanningCandidateComponentProvider scanningCandidateComponentProvider =
                new ClassPathScanningCandidateComponentProvider(false);
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            scanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        }
        return scanningCandidateComponentProvider.findCandidateComponents(basePackage);
    }

//    public void sort(List<MenuEntity> entities) {
//        entities.sort((o1, o2) -> (int) (o1.getSort() - o2.getSort()));
//        entities.forEach(menuEntity -> {
//            sort(menuEntity.getChildList());
//        });
//    }

    private SubSystemEntity getSubSystem(LmSubSystem lmSubSystem, Class<?> beanClazz) {
        SubSystemEntity subSystemEntity = new SubSystemEntity();
        subSystemEntity.setCode(StrUtil.isNotEmpty(lmSubSystem.code()) ?
                lmSubSystem.code() : StrUtil.toUnderlineCase(beanClazz.getSimpleName()));
        subSystemEntity.setName(lmSubSystem.name());
        subSystemEntity.setActive(true);
        subSystemEntity.setSort(lmSubSystem.sort());
        subSystemEntity.setZIndex(lmSubSystem.zIndex());
        return subSystemEntity;
    }

    private List<MenuEntity> getMenuDirEntity(LmMenuDir lmMenuDir, Class<?> beanClazz) {
        List<MenuEntity> menuEntities = new ArrayList<>();
        if (lmMenuDir == null) return new ArrayList<>();
        if (!lmMenuDir.active()) return new ArrayList<>();
        MenuEntity menuDir = new MenuEntity();
        String code = StrUtil.isNotEmpty(lmMenuDir.code()) ?
                lmMenuDir.code() : StrUtil.toUnderlineCase(beanClazz.getSimpleName());
        menuDir.setCode(code);
        menuDir.setName(StrUtil.isNotEmpty(lmMenuDir.name()) ? lmMenuDir.name() : code);
        menuDir.setIcon(StrUtil.isNotEmpty(lmMenuDir.icon()) ? lmMenuDir.icon() : "");
        menuDir.setSort(lmMenuDir.sort());
        menuDir.setActive(lmMenuDir.active());
        menuDir.setZIndex(lmMenuDir.zIndex());
        menuDir.setSystemCode(lmMenuDir.systemCode());

        menuEntities.add(menuDir);
        for (LmMenu lmMenu : lmMenuDir.menus()) {
            MenuEntity menuEntity = getMenuEntity(lmMenu, beanClazz);
            if (menuEntity != null) {
                menuEntity.setUpCode(code);
                menuEntities.add(menuEntity);
            }
        }
        return menuEntities;
    }


    private MenuEntity getMenuEntity(LmMenu lmMenu, Class<?> beanClazz) {
        if (lmMenu == null) return null;
        if (!lmMenu.active()) return null;
        MenuEntity menu = new MenuEntity();
        String code = StrUtil.isNotEmpty(lmMenu.code()) ?
                lmMenu.code() : StrUtil.toUnderlineCase(beanClazz.getSimpleName());
        menu.setCode(code);
        menu.setName(StrUtil.isNotEmpty(lmMenu.value()) ? lmMenu.value() : code);
        menu.setIcon(StrUtil.isNotEmpty(lmMenu.icon()) ? lmMenu.icon() : "");
        menu.setUri(StrUtil.isNotEmpty(lmMenu.uri()) ? lmMenu.uri() : "");
        menu.setTableCode(StrUtil.isNotEmpty(lmMenu.table()) ? lmMenu.table() : "");
        menu.setSort(lmMenu.sort());
        menu.setActive(lmMenu.active());
        menu.setZIndex(lmMenu.zIndex());
        menu.setUpCode(StrUtil.isNotEmpty(lmMenu.upCode()) ? lmMenu.upCode() : "");
        menu.setSystemCode(lmMenu.systemCode());
        return menu;
    }

}
