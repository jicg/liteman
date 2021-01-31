package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jicg on 2020/12/30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LmColumnAttr {
    boolean canNewVisible() default false;

    boolean canNew() default true;

    boolean canEditVisible() default true;

    boolean canEdit() default true;

    boolean canListVisible() default true;

    boolean canSearch() default false;

    boolean canExport() default false;

    boolean canImport() default false;
}

