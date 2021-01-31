package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * @author jicg on 2021/1/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LmMenu {
    String value() default "";

    String code() default "";

    String icon() default "";

    String uri() default "";

    long sort() default 1000L;

    long zIndex() default 1000L;

    boolean active() default true;
}
