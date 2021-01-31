package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmMenu;
import com.jicg.os.liteman.gen.anno.LmMenuDir;
import com.jicg.os.liteman.gen.anno.LmMenus;

/**
 * @author jicg on 2021/1/12
 */
//@LmMenuDir(code = MenuSysModule.SYSTEM_MENU, name = "系统模块", upCode = "")
@LmMenuDir(name = "系统管理",

        code = MenuSysModule.SYSTEM_MENU,
        menus = {
                @LmMenu(code = "sys_menu", value = "菜单"),
                @LmMenu(code = "sys_user", value = "用户"),
        })
@LmMenu(code = "sys_menu", value = "菜单")
@LmMenu(code = "sys_user", value = "用户")
public class MenuSysModule {
    public static final String SYSTEM_MENU = "SYSTEM_MENU";

    @LmMenuDir(name = "系统管理",

            upCode = MenuSysModule.SYSTEM_MENU,
            menus = {
                    @LmMenu(code = "sys_menu", value = "菜单"),
                    @LmMenu(code = "sys_user", value = "用户"),
            })
    public static class SysManager {
    }

    @LmMenuDir(name = "表管理",
            upCode = MenuSysModule.SYSTEM_MENU,
            menus = {
                    @LmMenu(code = "sys_table", value = "表"),
                    @LmMenu(code = "sys_column", value = "字段"),
            })
    public static class SysTableManager {
    }
}
