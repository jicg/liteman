package com.jicg.os.liteman.gen.service.scan;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jicg.os.liteman.gen.anno.LmMenu;
import com.jicg.os.liteman.gen.anno.LmMenuDir;
import com.jicg.os.liteman.gen.anno.LmMenuDirs;
import com.jicg.os.liteman.gen.anno.LmMenus;
import com.jicg.os.liteman.gen.service.LmService;
import com.jicg.os.liteman.orm.repository.MenuRepository;
import com.jicg.os.liteman.orm.system.MenuEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

/**
 * @author jicg on 2021/1/13
 */
@Slf4j
@Component
public class ScanMenu {
    public Map<String, MenuEntity> cache = new HashMap<>();
    final MenuRepository menuRepository;

    public ScanMenu(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void scanMenuPackages() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        //获取数据库中的 菜单
        List<MenuEntity> menuEntities = menuRepository.findAllByUpCodeIsNull();
        //获取数据库中的 菜单

//
//        cache = new HashMap<>();
//        menuEntities.forEach(it -> {
//            if (StrUtil.isNotEmpty(it.getUpCode())) {
//                MenuEntity menuEntity = menuRepository.getFirstByCode(it.getUpCode());
//                cache.put(it.getCode(),menuEntity);
//            }
//        });

        for (String basePackage : LmService.basePackages) {

            for (BeanDefinition beanDefinition : getScanBean(LmMenuDirs.class, basePackage)) {
                Class<?> beanClazz = Class.forName(beanDefinition.getBeanClassName());
                getMenuDirEntity(beanClazz);
            }

            for (BeanDefinition beanDefinition : getScanBean(LmMenus.class, basePackage)) {
                Class<?> beanClazz = Class.forName(beanDefinition.getBeanClassName());
                LmMenu[] lmMenus = beanClazz.getAnnotationsByType(LmMenu.class);
                for (LmMenu lmMenu : lmMenus) {
                    getMenuEntity(lmMenu, beanClazz);
                }
            }
        }

        System.out.println("=====" + JSONUtil.toJsonStr(cache));

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

    public Set<BeanDefinition> getScanBean(
            Class<? extends Annotation> annotationType, String basePackage) {
        ClassPathScanningCandidateComponentProvider scanningCandidateComponentProvider =
                new ClassPathScanningCandidateComponentProvider(false);
        scanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        return scanningCandidateComponentProvider.findCandidateComponents(basePackage);
    }

    public void sort(List<MenuEntity> entities) {
        entities.sort((o1, o2) -> (int) (o1.getSort() - o2.getSort()));
        entities.forEach(menuEntity -> {
            sort(menuEntity.getChildList());
        });
    }

    private void getMenuDirEntity(Class<?> beanClazz)
            throws IllegalAccessException, InstantiationException {

        LmMenuDir[] lmMenuDirs = beanClazz.getAnnotationsByType(LmMenuDir.class);
        Object beanObject = beanClazz.newInstance();
        for (LmMenuDir lmMenuDir : lmMenuDirs) {
            if (lmMenuDir != null && !lmMenuDir.active()) return;

            MenuEntity menuDir = new MenuEntity();
            String code = lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.code()) ?
                    lmMenuDir.code() : StrUtil.toUnderlineCase(beanClazz.getSimpleName());


//            final List<MenuEntity> menuEntityList = new ArrayList<>();

            menuDir.setCode(code);
            menuDir.setName(lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.name()) ? lmMenuDir.name() : code);
            menuDir.setIcon(lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.icon()) ? lmMenuDir.icon() : "");
            menuDir.setSort(lmMenuDir != null ? lmMenuDir.sort() : 1000L);
            menuDir.setActive(lmMenuDir == null || lmMenuDir.active());
            menuDir.setZIndex(lmMenuDir != null ? lmMenuDir.zIndex() : 1000L);

//            Arrays.stream(ReflectUtil.getFields(beanClazz)).forEach(field -> {
//                MenuEntity menuEntity = getMenuEntity(beanObject, field);
//                if (menuEntity == null) return;
//                menuEntityList.removeIf(it -> menuEntity.getCode().equals(it.getCode()));
//                menuEntityList.add(menuEntity);
//            });
//            menuDir.setChildList(menuDir.getChildList());

            if (lmMenuDir != null) {
                for (LmMenu lmMenu : lmMenuDir.menus()) {
                    getMenuEntity(lmMenu, beanClazz);
                }
            }

            if (cache.containsKey(code) && menuDir.getZIndex() <= cache.get(code).getZIndex()) return;
            cache.put(code, menuDir);
        }


    }


    private void getMenuEntity(LmMenu lmMenu, Class<?> beanClazz)
            throws IllegalAccessException, InstantiationException {
        if (lmMenu == null) return;
        if (!lmMenu.active()) return;

        MenuEntity menu = new MenuEntity();
        String code = StrUtil.isNotEmpty(lmMenu.code()) ?
                lmMenu.code() : StrUtil.toUnderlineCase(beanClazz.getSimpleName());

        menu.setCode(code);
        menu.setName(StrUtil.isNotEmpty(lmMenu.name()) ? lmMenu.name() : code);
        menu.setIcon(StrUtil.isNotEmpty(lmMenu.icon()) ? lmMenu.icon() : "");
        menu.setSort(lmMenu.sort());
        menu.setActive(lmMenu.active());
        menu.setZIndex(lmMenu.zIndex());

//            Arrays.stream(ReflectUtil.getFields(beanClazz)).forEach(field -> {
//                MenuEntity menuEntity = getMenuEntity(beanObject, field);
//                if (menuEntity == null) return;
//                menuEntityList.removeIf(it -> menuEntity.getCode().equals(it.getCode()));
//                menuEntityList.add(menuEntity);
//            });
//            menuDir.setChildList(menuDir.getChildList());


        menu.setUpCode(StrUtil.isNotEmpty(lmMenu.upCode()) ? lmMenu.upCode() : "");
        if (cache.containsKey(code) && menu.getZIndex() <= cache.get(code).getZIndex()) return;
        cache.put(code, menu);
    }

//    private String getMenuDirUpCode(Class<?> beanClazz) {
//        LmMenuDir lmMenuDir = beanClazz.getAnnotation(LmMenuDir.class);
//
//        String code = lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.upCode()) ?
//                lmMenuDir.upCode() : beanClazz.getEnclosingClass() == null ? "object" : StrUtil.toUnderlineCase(beanClazz.getEnclosingClass().getSimpleName());
//        return code;
//    }
//
//    private MenuEntity getMenuEntity(Object beanObject, Field field) {
//        if (field.getType() != String.class) return null;
//        LmMenu lmMenu = field.getAnnotation(LmMenu.class);
//        MenuEntity menuEntity = new MenuEntity();
//        menuEntity.setCode(lmMenu != null && StrUtil.isNotEmpty(lmMenu.code()) ? lmMenu.code() : field.getName());
//        menuEntity.setName(
//                lmMenu != null && StrUtil.isNotEmpty(lmMenu.value()) ? lmMenu.value()
//                        : (String) ReflectUtil.getFieldValue(beanObject, field));
//        menuEntity.setIcon(lmMenu != null && StrUtil.isNotEmpty(lmMenu.icon()) ? lmMenu.icon() : "");
//        menuEntity.setUri(lmMenu != null && StrUtil.isNotEmpty(lmMenu.uri()) ? lmMenu.uri() : "");
//        menuEntity.setSort(lmMenu != null ? lmMenu.sort() : 1000L);
//        menuEntity.setActive(lmMenu == null || lmMenu.active());
//        menuEntity.setZIndex(lmMenu != null ? lmMenu.zIndex() : 1000L);
//        return menuEntity;
//    }

}
