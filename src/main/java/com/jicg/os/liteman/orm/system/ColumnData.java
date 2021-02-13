package com.jicg.os.liteman.orm.system;

import com.jicg.os.liteman.orm.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author jicg on 2021/1/4
 */
public class ColumnData {
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Entity
   public static class Select  extends BaseEntity{
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
}
