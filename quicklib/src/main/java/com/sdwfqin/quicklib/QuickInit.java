package com.sdwfqin.quicklib;

import com.sdwfqin.quicklib.base.Constants;

/**
 * 描述：在Application中初始化
 *
 * @author 张钦
 * @date 2018/1/29
 */
public class QuickInit {

    public static void setRealPath(String realPath) {
        Constants.SAVE_REAL_PATH = realPath;
    }

    public static void setBaseUrl(String baseUrl) {
        Constants.BASE_URL = baseUrl;
    }

    public static void setLog(boolean log) {
        Constants.LOG_TYPE = log;
    }
}
