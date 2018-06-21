package com.sdwfqin.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/6/20
 */
public class WidgetUtils {

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
