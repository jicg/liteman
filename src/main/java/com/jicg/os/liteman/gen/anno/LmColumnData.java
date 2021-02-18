package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.*;

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
    @interface SelectUse {
        String code() default "";

        String def() default "";
    }

    @Repeatable(value = Selects.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
    public @interface Select {
        String label() default "";

        String code() default "";

        SelectOption[] options() default {};
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface Selects {
        Select[] value() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface SelectOption {
        String label() default "";

        String value() default "";
    }
}
