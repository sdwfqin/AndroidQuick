package com.sdwfqin.quickseed.retrofit;

import android.content.Context;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/2/10
 */
public class BaseResponse<T> {

    private int code;
    private String msg;
    private T res;

    public boolean isOk(Context context) {
        if (code == 200) {
            return true;
        } else {
            NetworkError.error(context, new ServerException(code, msg));
            return false;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }
}
