package com.sdwfqin.quickseed.retrofit;

import android.content.Context;
import android.widget.Toast;

/**
 * 描述：网络统一异常处理
 *
 * @author zhangqin
 * @date 2018/2/10
 */
public class NetworkError {

    /**
     * @param context 可以用于跳转Activity等操作
     */
    public static void error(Context context, Throwable throwable) {
        RetrofitException.ResponeThrowable responeThrowable = RetrofitException.retrofitException(throwable);
        // 此处可以通过判断错误代码来实现根据不同的错误代码做出相应的反应
        switch (responeThrowable.code) {
            case RetrofitException.ERROR.UNKNOWN:
            case RetrofitException.ERROR.PARSE_ERROR:
            case RetrofitException.ERROR.NETWORD_ERROR:
            case RetrofitException.ERROR.HTTP_ERROR:
            case RetrofitException.ERROR.SSL_ERROR:
                Toast.makeText(context, responeThrowable.message, Toast.LENGTH_SHORT).show();
                break;
            case -1:
                // 跳转到登陆页面
                break;
            default:
                Toast.makeText(context, responeThrowable.message, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
