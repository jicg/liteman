package com.jicg.os.liteman.gen.service.scan;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.jicg.os.liteman.gen.anno.LmMenu;
import com.jicg.os.liteman.gen.anno.LmMenuDir;
import com.jicg.os.liteman.gen.service.LmService;
import com.jicg.os.liteman.orm.repository.MenuRepository;
import com.jicg.os.liteman.orm.system.MenuEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

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

    public void scanMenuPackages(List<MenuEntity> menuEntities) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        cache = new HashMap<>();
        menuEntities.forEach(it -> {
            if (StrUtil.isNotEmpty(it.getUpCode())) {
                MenuEntity menuEntity = menuRepository.getFirstByCode(it.getUpCode());
                cache.put(it.getCode(),menuEntity);
            }
        });
        ClassPathScanningCandidateComponentProvider scanningCandidateComponentProvider =
                new ClassPathScanningCandidateComponentProvider(false);
        for (String basePackage : LmService.basePackages) {
            scanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(LmMenuDir.class));
            Set<BeanDefinition> beanDefinitions = scanningCandidateComponentProvider.findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> beanClazz = Class.forName(beanDefinition.getBeanClassName());
                getMenuDirEntity(beanClazz);
            }
        }
        Map<String, MenuEntity> idMenus = new HashMap<>();
        menuEntities.forEach(menuEntity -> {
            idMenus.put(menuEntity.getUpCode(), menuEntity);
        });
        menuEntities.forEach(menuEntity -> {
            if (StrUtil.isEmpty(menuEntity.getUpCode())) return;
            idMenus.get(menuEntity.getUpCode()).getChildList().add(menuEntity);
        });
        List<String> notTops = new ArrayList<>();
        cache.forEach((code, menu) -> {
            log.info(code + "   " + menu.getUpCode());
            if (cache.containsKey(menu.getUpCode())) {
                MenuEntity itemUp = cache.get(menu.getUpCode());
                itemUp.getChildList().add(menu);
            }
            if (!menu.getUpCode().equals("object")) {
                notTops.add(code);
            }
        });
        notTops.forEach(str -> {
            cache.remove(str);
        });
        cache.forEach((code, menu) -> {
            LmService.MENU_ENTITIES.add(menu);
        });
        sort(LmService.MENU_ENTITIES);

    }

    public void sort(List<MenuEntity> entities) {
        entities.sort((o1, o2) -> (int) (o1.getSort() - o2.getSort()));
        entities.forEach(menuEntity -> {
            sort(menuEntity.getChildList());
        });
    }

    private void getMenuDirEntity(Class<?> beanClazz)
            throws IllegalAccessException, InstantiationException {

        LmMenuDir lmMenuDir = beanClazz.getAnnotation(LmMenuDir.class);
        if (lmMenuDir != null && !lmMenuDir.active()) return;
        Object beanObject = beanClazz.newInstance();
        MenuEntity menuDir = new MenuEntity();
        String code = lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.code()) ?
                lmMenuDir.code() : StrUtil.toUnderlineCase(beanClazz.getSimpleName());
        final List<MenuEntity> menuEntityList = new ArrayList<>();

        menuDir.setCode(code);
        menuDir.setName(lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.name()) ? lmMenuDir.name() : code);
        menuDir.setIcon(lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.icon()) ? lmMenuDir.icon() : "");
        menuDir.setSort(lmMenuDir != null ? lmMenuDir.sort() : 1000L);
        menuDir.setActive(lmMenuDir == null || lmMenuDir.active());
        menuDir.setZIndex(lmMenuDir != null ? lmMenuDir.zIndex() : 1000L);

        Arrays.stream(ReflectUtil.getFields(beanClazz)).forEach(field -> {
            MenuEntity menuEntity = getMenuEntity(beanObject, field);
            if (menuEntity == null) return;
            menuEntityList.removeIf(it -> menuEntity.getCode().equals(it.getCode()));
            menuEntityList.add(menuEntity);
        });
        menuDir.setChildList(menuEntityList);
        if (cache.containsKey(code) && menuDir.getZIndex() <= cache.get(code).getZIndex()) return;
        cache.put(code, menuDir);
    }

    private String getMenuDirUpCode(Class<?> beanClazz) {
        LmMenuDir lmMenuDir = beanClazz.getAnnotation(LmMenuDir.class);

        String code = lmMenuDir != null && StrUtil.isNotEmpty(lmMenuDir.upCode()) ?
                lmMenuDir.upCode() : beanClazz.getEnclosingClass() == null ? "object" : StrUtil.toUnderlineCase(beanClazz.getEnclosingClass().getSimpleName());
        return code;
    }

    private MenuEntity getMenuEntity(Object beanObject, Field field) {
        if (field.getType() != String.class) return null;
        LmMenu lmMenu = field.getAnnotation(LmMenu.class);
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setCode(lmMenu != null && StrUtil.isNotEmpty(lmMenu.code()) ? lmMenu.code() : field.getName());
        menuEntity.setName(
                lmMenu != null && StrUtil.isNotEmpty(lmMenu.value()) ? lmMenu.value()
                        : (String) ReflectUtil.getFieldValue(beanObject, field));
        menuEntity.setIcon(lmMenu != null && StrUtil.isNotEmpty(lmMenu.icon()) ? lmMenu.icon() : "");
        menuEntity.setUri(lmMenu != null && StrUtil.isNotEmpty(lmMenu.uri()) ? lmMenu.uri() : "");
        menuEntity.setSort(lmMenu != null ? lmMenu.sort() : 1000L);
        menuEntity.setActive(lmMenu == null || lmMenu.active());
        menuEntity.setZIndex(lmMenu != null ? lmMenu.zIndex() : 1000L);
        return menuEntity;
    }

}
