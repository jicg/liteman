package com.jicg.os.liteman.gen.anno;

import java.lang.annotation.*;

/**
 * @author jicg on 2021/1/12
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface LmMenus {
    LmMenu[] value() default {};


}
