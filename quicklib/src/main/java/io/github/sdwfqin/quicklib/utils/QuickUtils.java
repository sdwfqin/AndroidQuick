package io.github.sdwfqin.quicklib.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：工具类
 *
 * @author 张钦
 * @date 2018/1/29
 */
public class QuickUtils {

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return outFormat.format(now);
    }
}
