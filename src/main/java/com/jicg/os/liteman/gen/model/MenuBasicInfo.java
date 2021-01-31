package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmMenu;
import com.jicg.os.liteman.gen.anno.LmMenuDir;

/**
 * @author jicg on 2021/1/18
 */
@LmMenuDir(name = "基础资料", upCode = "")
public class MenuBasicInfo {
    @LmMenuDir(name = "商品管理",menus = {
            @LmMenu(code = "basic_menu",value="属性"),
            @LmMenu(code = "basic_user",value="商品"),
    })
    public static class ProductManager {
    }
}
