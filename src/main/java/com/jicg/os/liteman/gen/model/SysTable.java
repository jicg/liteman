package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmColumn;
import com.jicg.os.liteman.gen.anno.LmColumnAttr;
import com.jicg.os.liteman.gen.anno.LmIgnore;
import com.jicg.os.liteman.gen.anno.LmTable;
import com.jicg.os.liteman.orm.system.ColumnType;

import java.util.Date;

/**
 * @author jicg on 2020/12/30
 */
@LmTable(
        name = "sys_table",
        label = "表"
)
public class SysTable {
    @LmColumn(label = "数据库字段名")
    private String name;
    @LmColumn(label = "页面名称")
    private String label;
    @LmColumn(label = "备注")
    private String description;

    @LmColumn(label = "备注2",columnType = ColumnType.Select)
    @LmColumnAttr()
    private String description2;

    @LmColumn(label = "日期",columnType = ColumnType.Date)
    private Date ownerDate;
}
