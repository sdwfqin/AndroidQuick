package io.github.sdwfqin.quickseed.data.bean

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
)