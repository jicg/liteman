package com.jicg.os.liteman.gen.anno;

import com.jicg.os.liteman.gen.scan.LmBindScannerRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LmBindScannerRegistrar.class})
public @interface LmScan {
    String[] value() default { "com.jicg.os.liteman" };
}
