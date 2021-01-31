package com.jicg.os.liteman;

import com.jicg.os.liteman.orm.repository.TableRepository;
import com.jicg.os.liteman.orm.system.ColumnEntity;
import com.jicg.os.liteman.orm.system.ColumnType;
import com.jicg.os.liteman.orm.system.TableEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@SpringBootTest
//@DataJpaTest
//@ExtendWith(SpringExtension.class)
class LitemanApplicationTests {

    @Autowired
    private TableRepository tableRepository;

    @Test
    void contextLoads() {

    }

}
