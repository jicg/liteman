package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.*;

/**
 * @author jicg on 2021/1/12
 */
@Repeatable(value = LmMenuDirs.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface LmMenuDir {

    String code() default "";

    String name();

    String upCode() default "";

    boolean active() default true;

    String icon() default "";

    long sort() default 1000L;

    long zIndex() default 1000L;

    LmMenuUp upMenu() default @LmMenuUp();

    LmMenu[] menus() default {};
}
