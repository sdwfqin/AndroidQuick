package com.sdwfqin.quickseed.retrofit;

/**
 * 描述：服务器下发的错误
 *
 * @author zhangqin
 * @date 2018/2/27
 */
public class ServerException extends RuntimeException {

    public int code;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
    }
}
