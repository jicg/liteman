package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jicg on 2021/2/7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LmSubSystem {
    String code() default "";

    String icon() default "";

    String name() default "";

    long zIndex() default 1000;
    long sort() default 1000;
}
