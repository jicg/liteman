package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmMenu;
import com.jicg.os.liteman.gen.anno.LmMenuDir;
import com.jicg.os.liteman.gen.anno.LmSubSystem;

/**
 * @author jicg on 2021/1/12
 */
@LmSubSystem(code = SysMenu.SYSTEM_MODULE, name = "系统管理", zIndex = 1000)

@LmMenuDir(name = "系统管理",
        code = SysMenu.SYSTEM_MENU,
        menus = {
                @LmMenu(code = "sys_menu1", value = "菜单1", table = "sys_table1"),
        })
@LmMenuDir(name = "业务管理",
        code = SysMenu.SYSTEM_MENU2,
        menus = {
                @LmMenu(code = "sys_menu2", value = "菜单2", table = "sys_table2"),
                @LmMenu(code = "sys_menu3", value = "菜单3", table = "sys_table3"),
                @LmMenu(code = "sys_menu4", value = "菜单4", table = "sys_table4"),
                @LmMenu(code = "sys_menu5", value = "菜单5", table = "sys_table5"),
                @LmMenu(code = "sys_menu6", value = "菜单6", table = "sys_table6"),
                @LmMenu(code = "sys_menu7", value = "菜单7", table = "sys_table7"),
                @LmMenu(code = "sys_menu8", value = "菜单8", table = "sys_table8"),
                @LmMenu(code = "sys_menu9", value = "菜单9", table = "sys_table9"),
        })

@LmMenu(code = "sys_menu", value = "菜单", table = "sys_table")
public class SysMenu {
    public static final String SYSTEM_MODULE = "system";
    public static final String SYSTEM_MENU = "system";
    public static final String SYSTEM_MENU2 = "system2";
}
