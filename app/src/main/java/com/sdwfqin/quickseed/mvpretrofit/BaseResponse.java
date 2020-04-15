package com.sdwfqin.quickseed.mvpretrofit;

import com.sdwfqin.quicklib.mvp.BaseView;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/2/10
 */
public class BaseResponse<T> {


    private int statusCode;
    private String message;
    private T data;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isOk(BaseView view) {
        if (statusCode == 1) {
            return true;
        } else {
            NetworkError.error(view, new ServerException(statusCode, message));
            return false;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
