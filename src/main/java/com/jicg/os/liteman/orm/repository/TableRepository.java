package com.jicg.os.liteman.orm.repository;

import com.jicg.os.liteman.orm.system.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jicg on 2020/12/28
 */

public interface TableRepository extends JpaRepository<TableEntity,Long> {
}
