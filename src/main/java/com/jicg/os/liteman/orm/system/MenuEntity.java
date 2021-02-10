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
public class MenuEntity extends SysBaseEntity {
    @Column(unique = true)
    private String code;
    private String name;
    private String icon;
    private Long sort;
    private String tableCode;
    private String uri;
    private String description;
    private Long zIndex = 1000L;
    private String systemCode = "";
    //    @Column(insertable = false, updatable = false)
//    private Long menuCode;
//    @Column(insertable = false, updatable = false)
    private String upCode = "";
//    private Long menuId;
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "menu_id")
//    private MenuEntity menuUp;

    //    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
////    @JoinColumn(name = "menuCode", referencedColumnName = "code")
//    @JoinColumn(name = "menuId")
//    private Set<MenuEntity> childList = new HashSet<>();
    @Transient
    private List<MenuEntity> childList = new ArrayList<>();
}
