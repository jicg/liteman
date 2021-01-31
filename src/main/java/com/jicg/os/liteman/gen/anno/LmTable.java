package com.jicg.os.liteman.gen.anno;


import java.lang.annotation.*;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LmTable {
    String name() default "";

    String label() default "";

    String description() default "";

    LmButton[] buttons() default {};
}
