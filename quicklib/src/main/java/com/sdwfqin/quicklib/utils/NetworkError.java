package com.sdwfqin.quicklib.utils;

import android.content.Context;

/**
 * 描述：网络错误处理类
 * <p>
 * 如果需要统一的网络错误处理，请实现此类并且使用BaseActivity/BaseFragment中的networkError方法
 *
 * @author 张钦
 * @date 2018/1/29
 */
public abstract class NetworkError {

    /**
     * 网络错误
     */
    public static void networkError(Context context) {

    }

    /**
     * 网络错误的统一异常处理
     *
     * @param errorCode 错误代码
     * @param errorMsg  错误消息
     */
    public static void networkError(Context context, int errorCode, String errorMsg) {

    }
}
