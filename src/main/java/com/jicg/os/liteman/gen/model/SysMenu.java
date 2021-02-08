package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmMenu;
import com.jicg.os.liteman.gen.anno.LmMenuDir;
import com.jicg.os.liteman.gen.anno.LmSubSystem;

/**
 * @author jicg on 2021/1/12
 */
@LmSubSystem(code = SysMenu.SYSTEM_MODULE, name = "系统管理",zIndex = 1000)
@LmMenuDir(code = SysMenu.SYSTEM_MENU, name = "系统模块")
@LmMenuDir(name = "系统管理",
        code = SysMenu.SYSTEM_MENU,
        menus = {
                @LmMenu(code = "sys_menu", value = "菜单"),
                @LmMenu(code = "sys_user", value = "用户"),
        })
@LmMenu(code = "sys_menu", value = "菜单")
@LmMenu(code = "sys_user", value = "用户", upCode = "")
public class SysMenu {
    public static final String SYSTEM_MODULE = "SYSTEM_MODULE";
    public static final String SYSTEM_MENU = "SYSTEM_MENU";
    @LmSubSystem(code = SysMenu.SYSTEM_MODULE, name = "系统管理2",zIndex = 1001)
    public static class UserMod{

    }
}
