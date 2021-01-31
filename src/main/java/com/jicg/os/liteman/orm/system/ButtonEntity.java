package com.jicg.os.liteman.orm.system;

import com.jicg.os.liteman.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author jicg on 2021/1/5
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ButtonEntity extends SysBaseEntity {
    private String name;
    private String label;
    private ButtonType buttonType = ButtonType.List;

    private String checkPermScript;
    private String script;


}