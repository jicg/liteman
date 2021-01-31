package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.*;


/**
 * @author jicg on 2020/12/30
 */
@LmTable(
        label = "字段",
        buttons = {@LmButton(label = "测试", name = "name")}
)
public class SysColumn {
    @LmColumn(label = "数据库字段名")
    private String name;
    @LmColumn(label = "页面名称")
    private String label;
    @LmColumn(label = "备注")
    private String description;
    @LmColumnLink(tableName = "SysTable")
    private Long tableId;
}
