package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jicg on 2021/1/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LmMenuDir {

    String code() default "";

    String name();

    String upCode() default "";

    boolean active() default true;

    String icon() default "";

    long sort() default 1000L;

    long zIndex() default 1000L;
}
