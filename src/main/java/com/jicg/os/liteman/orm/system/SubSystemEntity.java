package com.jicg.os.liteman.orm.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jicg on 2020/12/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class SubSystemEntity extends SysBaseEntity {
    @Column(unique = true)
    private String code = "";
    private String name = "";
    private String icon = "";
    private long zIndex = 1000;
    private long sort = 1000;

}
