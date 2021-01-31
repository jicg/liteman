package com.jicg.os.liteman.orm.system;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jicg on 2021/1/12
 */
@Data
@MappedSuperclass
public class SysBaseEntity implements Serializable {
    @TableGenerator(name = "SysEntity_Gen", table = "ID_GEN",
            pkColumnName = "GEN_NAME",
            valueColumnName = "GEN_VAL"
            , initialValue = 10000)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SysEntity_Gen")
    private long id;
    @CreationTimestamp
    private Date cDate;
    @UpdateTimestamp
    private Date uDate;
    private boolean isActive = false;
}
