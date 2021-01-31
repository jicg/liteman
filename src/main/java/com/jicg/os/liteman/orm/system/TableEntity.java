package com.jicg.os.liteman.orm.system;

import com.jicg.os.liteman.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jicg on 2020/12/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TableEntity extends SysBaseEntity {
    @Column(unique = true)
    private String name;
    private String label;
    private String description;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id")
    private Set<ColumnEntity> columnEntityList = new HashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "table_id")
    private Set<ButtonEntity> buttonEntityList = new HashSet<>();


    //    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_cate_id")
    private TableCateEntity tableCateEntity;
}
