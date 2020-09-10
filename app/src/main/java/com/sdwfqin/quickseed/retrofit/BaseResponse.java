package com.sdwfqin.quickseed.retrofit;

import android.content.Context;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/2/10
 */
public class BaseResponse<T> {


    private String method;
    private String level;
    private String code;
    private String description;
    private T data;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "method='" + method + '\'' +
                ", level='" + level + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isOk(Context context) {
        if ("000".equals(code)) {
            return true;
        } else if ("002".equals(code)) {
            // 无数据
            return false;
        } else {
            NetworkError.error(context, new ServerException(Integer.parseInt(code), description));
            return false;
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
