package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmMenuDir;

/**
 * @author jicg on 2021/1/12
 */
@LmMenuDir(name = "系统模块", upCode = "object")
public class MenuSysModule {
    @LmMenuDir(name = "系统管理")
    public static class SysManager {
        private final String sys_menu = "菜单";
        private final String sys_user = "用户";
    }

    @LmMenuDir(name = "表管理")
    public static class SysTableManager {
        private final String sys_table = "表";
        private final String sys_column = "字段";
    }
}
