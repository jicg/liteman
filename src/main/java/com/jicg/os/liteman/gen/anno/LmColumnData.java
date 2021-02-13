package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface LmColumnData {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Link {
        String tableName();

        String columnName() default "id";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Attr {
        boolean canNewVisible() default false;

        boolean canNew() default true;

        boolean canEditVisible() default true;

        boolean canEdit() default true;

        boolean canListVisible() default true;

        boolean canSearch() default false;

        boolean canExport() default false;

        boolean canImport() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Select {
        String label() default "";

        String code() default "";

        SelectOption[] option() default {};

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface SelectOption {
        String label() default "";
        String value() default "";
    }
}
