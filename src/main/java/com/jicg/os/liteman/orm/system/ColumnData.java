package com.jicg.os.liteman.orm.system;

import com.jicg.os.liteman.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jicg on 2021/1/4
 */
public class ColumnData {
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Entity
    public static class Select extends BaseEntity {
        private String label;
        private String code;
        @LazyCollection(LazyCollectionOption.FALSE)
        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JoinColumn(name = "select_id")
        private Set<SelectOption> buttonEntityList = new HashSet<>();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Entity
    public static class SelectOption extends BaseEntity {
        private String label;
        private String value;
    }


    @EqualsAndHashCode(callSuper = true)
    @Data
    @Entity
    public static class ColumnLink extends BaseEntity {
        private String tableName;
        private String columnName;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Entity
    public static class ColumnAttr extends BaseEntity {
        private boolean canNewVisible = true;
        private boolean canNew = true;
        private boolean canEditVisible = true;
        private boolean canEdit = true;

        private boolean canListVisible = true;
        private boolean canSearch = false;
        private boolean canExport = false;
        private boolean canImport = false;

    }

}
