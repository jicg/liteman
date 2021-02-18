package com.jicg.os.liteman.gen.anno;

import com.jicg.os.liteman.orm.system.ColumnData;
import com.jicg.os.liteman.orm.system.ColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jicg on 2020/12/29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LmColumn {
    String name() default "";

    String label() default "";

    String description() default "";

    ColumnData.Type columnType() default ColumnData.Type.Auto;

    String defVal() default "";

}