package com.sdwfqin.quickseed.retrofit;

import com.blankj.utilcode.util.ToastUtils;

/**
 * 描述：网络统一异常处理
 *
 * @author zhangqin
 * @date 2018/2/10
 */
public class NetworkError {

    public static void error(Throwable throwable) {
        RetrofitException.ResponeThrowable responeThrowable = RetrofitException.retrofitException(throwable);
        ToastUtils.showShort(responeThrowable.message);
    }

    public static void error(String msg, int errorCode) {
        ToastUtils.showShort(msg);
    }
}
