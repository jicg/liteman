package com.jicg.os.liteman.gen.model;

import com.jicg.os.liteman.gen.anno.LmMenuDir;

/**
 * @author jicg on 2021/1/18
 */
@LmMenuDir(name = "基础资料", upCode = "object")
public class MenuBasicInfo {
    @LmMenuDir(name = "商品管理")
    public static class ProductManager {
        private final String basic_menu = "属性";
        private final String basic_user = "商品";
    }
}
