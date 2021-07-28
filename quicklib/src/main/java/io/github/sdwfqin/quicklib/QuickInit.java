package io.github.sdwfqin.quicklib;

import io.github.sdwfqin.quicklib.base.QuickConstants;

/**
 * 描述：在Application中初始化
 *
 * @author 张钦
 * @date 2018/1/29
 */
public class QuickInit {

    /**
     * 设置文件保存位置
     *
     * @param realPath
     */
    public static void setRealPath(String realPath) {
        QuickConstants.SAVE_REAL_PATH = realPath;
    }

    /**
     * 设置图片的根路径
     *
     * @param baseUrl
     */
    public static void setBaseUrl(String baseUrl) {
        QuickConstants.BASE_URL = baseUrl;
    }
}
