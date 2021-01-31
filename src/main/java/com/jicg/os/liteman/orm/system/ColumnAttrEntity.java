package com.jicg.os.liteman.orm.system;

import com.jicg.os.liteman.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jicg on 2020/12/28
 */
@Data
@Embeddable
public class ColumnAttrEntity {
//    @ElementCollection
//    private List<String> columns = new ArrayList<>();
//    @ElementCollection
//    private List<String> linkColumns = new ArrayList<>();

    private boolean canNewVisible = true;
    private boolean canNew = true;
    private boolean canEditVisible = true;
    private boolean canEdit = true;

    private boolean canListVisible = true;
    private boolean canSearch = false;
    private boolean canExport = false;
    private boolean canImport = false;
}
