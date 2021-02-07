package com.jicg.os.liteman.orm.repository;

import com.jicg.os.liteman.orm.system.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jicg on 2020/12/28
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    //    List<MenuEntity> findAllByMenuIdIsNull();
    List<MenuEntity> findAllByCodeIsNull();
    List<MenuEntity> findAllByUpCodeIsNull();

    MenuEntity getFirstByCode(String code);
}
