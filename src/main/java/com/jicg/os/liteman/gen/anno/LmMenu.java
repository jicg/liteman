package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.*;
import java.util.List;

/**
 * @author jicg on 2021/1/12
 */
@Repeatable(value = LmMenus.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface LmMenu {
    String value() default "";

    String name() default "";

    String code() default "";

    String upCode() default "";

    String icon() default "";

    String uri() default "";

    String systemCode() default "";

    long sort() default 1000L;

    long zIndex() default 1000L;

    boolean active() default true;

}
