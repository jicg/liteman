package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmColumn;
import com.jicg.os.liteman.gen.anno.LmColumnData;
import com.jicg.os.liteman.gen.anno.LmTable;
import com.jicg.os.liteman.orm.system.ColumnData;

import java.util.Date;

/**
 * @author jicg on 2020/12/30
 */

@LmTable(
        name = "sys_test",
        label = "测试表"
)
public class SysTest {
    @LmColumn(label = "测试名称")
    private String name;
}
