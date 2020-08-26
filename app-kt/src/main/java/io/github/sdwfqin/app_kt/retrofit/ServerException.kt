package io.github.sdwfqin.app_kt.retrofit

/**
 * 描述：服务器下发的错误
 *
 * @author zhangqin
 * @date 2018/2/27
 */
class ServerException(var code: Int, message: String) : RuntimeException(message)