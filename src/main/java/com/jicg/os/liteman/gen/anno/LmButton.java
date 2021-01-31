package com.jicg.os.liteman.gen.anno;

import com.jicg.os.liteman.orm.system.ButtonType;
import com.jicg.os.liteman.orm.system.ColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jicg on 2020/12/29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LmButton {
    String name() default "";

    String label() default "";

    String description() default "";

    ButtonType buttonType() default ButtonType.List;

    String checkPermScript() default "";

    String script() default "";

}