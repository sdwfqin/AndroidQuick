package com.sdwfqin.quickseed.mvpretrofit;

import com.sdwfqin.quicklib.mvp.IBaseView;

/**
 * 描述：网络统一异常处理
 *
 * @author zhangqin
 * @date 2018/2/10
 */
public class NetworkError {

    public static void error(IBaseView view, Throwable throwable) {
        RetrofitException.ResponeThrowable responeThrowable = RetrofitException.retrofitException(throwable);
        // 此处可以通过判断错误代码来实现根据不同的错误代码做出相应的反应
        switch (responeThrowable.code) {
            case RetrofitException.ERROR.UNKNOWN:
            case RetrofitException.ERROR.PARSE_ERROR:
            case RetrofitException.ERROR.NETWORD_ERROR:
            case RetrofitException.ERROR.HTTP_ERROR:
            case RetrofitException.ERROR.SSL_ERROR:
                view.showMsg(responeThrowable.message);
                break;
            case -1:
                // 跳转到登陆页面
                break;
            default:
                view.showMsg(responeThrowable.message);
                break;
        }
    }
}
