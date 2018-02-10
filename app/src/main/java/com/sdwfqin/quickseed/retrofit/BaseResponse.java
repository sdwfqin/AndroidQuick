package com.sdwfqin.quickseed.retrofit;

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

    public boolean isOk() {
        if (code == 200) {
            return true;
        } else {
            NetworkError.error(msg, code);
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
