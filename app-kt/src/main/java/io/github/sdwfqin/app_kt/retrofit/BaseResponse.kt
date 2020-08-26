package io.github.sdwfqin.app_kt.retrofit

import android.content.Context

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/2/10
 */
data class BaseResponse<T>(
        val method: String,
        val level: String,
        val code: String,
        val description: String,
        val data: T
) {

    fun isOk(context: Context): Boolean {
        return if (code == "000") {
            true
        } else if (code == "002") {
            // 无数据
            false
        } else {
            NetworkError.error(context, ServerException(code.toInt(), description))
            false
        }
    }
}