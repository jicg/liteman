package com.jicg.os.liteman.orm.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author jicg on 2021/1/4
 */
@Data
@Embeddable
public class ColumnLinkEntity {
    private String tableName;
    private String columnName;
}
