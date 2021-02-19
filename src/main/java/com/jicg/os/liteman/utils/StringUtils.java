package com.jicg.os.liteman.utils;

/**
 * @author jicg on 2021/2/19
 */

public class StringUtils {
    public static String[] commaSplit(String s) {
        StringBuilder str = new StringBuilder();
        String[] cols = s.split(";");
        String[] strs = new String[cols.length];
        for (int i = 0; i < cols.length; i++) {
            str.append(cols[i]);
            strs[i] = str.toString();
            str.append(";");
        }
        return strs;
    }

    public static String commaLast(String key) {
        return key.contains(";") ? key.substring(key.lastIndexOf(";") + 1) : key;
    }

    public static String commaRemoveLast(String key) {
        int index =key.lastIndexOf(";");
        if (index <= 0) return "";
        return key.substring(0, index);
    }
}
