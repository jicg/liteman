package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jicg on 2021/1/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface LmMenuDirs {

    LmMenuDir[] value() default {};
}
