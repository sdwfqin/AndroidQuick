package com.sdwfqin.quicklib.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：工具类
 *
 * @author 张钦
 * @date 2018/1/29
 */
public class QuickUtils {

    /**
     * 分割字符串
     *
     * @param str
     * @return
     */
    public static List<String> subStringToList(String str) {
        List<String> result = new ArrayList<>();
        if (!str.isEmpty()) {
            int len = str.length();
            if (len <= 1) {
                result.add(str);
                return result;
            } else {
                for (int i = 0; i < len; i++) {
                    result.add(str.substring(i, i + 1));
                }
            }
        }
        return result;
    }
}
