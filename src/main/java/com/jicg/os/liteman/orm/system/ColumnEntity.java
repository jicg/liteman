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
    private ColumnType columnType = ColumnType.Auto;
    private String defVal;
    @Embedded
    private ColumnAttrEntity columnAttrEntity = new ColumnAttrEntity();
    @Embedded
    private ColumnLinkEntity columnLink;

    @OneToOne
    private ColumnData.Select columnSelect;

}

