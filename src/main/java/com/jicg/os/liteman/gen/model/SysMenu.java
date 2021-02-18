package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmMenu;
import com.jicg.os.liteman.gen.anno.LmMenuDir;
import com.jicg.os.liteman.gen.anno.LmSubSystem;

/**
 * @author jicg on 2021/1/12
 */
@LmSubSystem(code = SysMenu.SYSTEM_MODULE, name = "系统管理", zIndex = 1000)
@LmMenu(code = "sys_table", value = "表", table = "sys_table")
@LmMenu(code = "sys_column", value = "字段", table = "sys_column")
public class SysMenu {
    public static final String SYSTEM_MODULE = "system";
    public static final String SYSTEM_MENU = "system";
    public static final String SYSTEM_MENU2 = "system2";
}
