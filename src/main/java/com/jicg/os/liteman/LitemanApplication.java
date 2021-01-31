package com.jicg.os.liteman;

import com.jicg.os.liteman.gen.anno.LmScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@LmScan//("com.jicg.os.liteman")
public class LitemanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LitemanApplication.class, args);
    }
}
