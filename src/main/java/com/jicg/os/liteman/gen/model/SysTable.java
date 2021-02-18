package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.*;
import com.jicg.os.liteman.orm.system.ColumnData;
import com.jicg.os.liteman.orm.system.ColumnType;

import java.util.Date;

/**
 * @author jicg on 2020/12/30
 */
@LmColumnData.Select(
        code = "isActive",
        label = "是否可用",
        options = {
                @LmColumnData.SelectOption(
                        label = "是",
                        value = "Y"
                ),
                @LmColumnData.SelectOption(
                        label = "否",
                        value = "N"
                )
        }
)

@LmTable(
        name = "sys_table",
        label = "表"
)
public class SysTable {
    @LmColumn(label = "表名")
    private String name;
    @LmColumn(label = "页面名称")
    private String label;
    @LmColumn(label = "备注")
    private String description;

    @LmColumn(label = "是否可用", columnType = ColumnData.Type.Select, defVal = "Y")
    @LmColumnData.SelectUse(code = "isActive", def = "Y")
    private String isActive;

    @LmColumn(label = "日期", columnType = ColumnData.Type.Date)
    private Date ownerDate;
}
