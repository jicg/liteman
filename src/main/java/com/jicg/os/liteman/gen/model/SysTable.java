package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmColumn;
import com.jicg.os.liteman.gen.anno.LmIgnore;
import com.jicg.os.liteman.gen.anno.LmTable;

/**
 * @author jicg on 2020/12/30
 */
@LmTable(
        label = "表"
)
public class SysTable {
    @LmColumn(label = "数据库字段名")
    private String name;
    @LmColumn(label = "页面名称")
    private String label;
    @LmColumn(label = "备注")
    private String description;
}
