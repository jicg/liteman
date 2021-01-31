package com.jicg.os.liteman.orm.system;

import com.jicg.os.liteman.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author jicg on 2021/1/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TableCateEntity extends BaseEntity {
    @Column(unique = true)
    private String title;
    private String icon;
    private String description;


}
