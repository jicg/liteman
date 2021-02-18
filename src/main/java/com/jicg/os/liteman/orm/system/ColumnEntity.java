package com.jicg.os.liteman.orm.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author jicg on 2020/12/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ColumnEntity extends SysBaseEntity {
    private long id;
    @Column(unique = true)
    private String name;
    private String label;
    private String description;
    private ColumnData.Type columnType = ColumnData.Type.Auto;
    private String defVal;
    @OneToOne
    private ColumnData.ColumnAttr columnAttrEntity = new ColumnData.ColumnAttr();
    @OneToOne
    private ColumnData.ColumnLink columnLink;

    @Embedded
    private ColumnData.SelectUse selectUse;


}

