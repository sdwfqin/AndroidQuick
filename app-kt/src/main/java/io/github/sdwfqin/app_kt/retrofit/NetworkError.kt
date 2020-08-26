package io.github.sdwfqin.app_kt.retrofit

import android.content.Context
import android.widget.Toast

/**
 * 描述：网络统一异常处理
 *
 * @author zhangqin
 * @date 2018/2/10
 */
object NetworkError {
    /**
     * @param context 可以用于跳转Activity等操作
     */
    @JvmStatic
    fun error(context: Context, throwable: Throwable) {
        val responseThrowable = RetrofitException.retrofitException(throwable)
        when (responseThrowable.code) {
            RetrofitException.ERROR.UNKNOWN,
            RetrofitException.ERROR.PARSE_ERROR,
            RetrofitException.ERROR.NETWORD_ERROR,
            RetrofitException.ERROR.HTTP_ERROR,
            RetrofitException.ERROR.SSL_ERROR
            ->
                Toast.makeText(context, responseThrowable.message, Toast.LENGTH_SHORT).show()
            -1 -> {
            }
            else -> Toast.makeText(context, responseThrowable.message, Toast.LENGTH_SHORT).show()
        }
    }
}