package com.sdwfqin.paylib.interfaces;

/**
 * 描述：支付回调接口
 *
 * @author 张钦
 * @date 2018/1/25
 */
public interface OnRequestListener {
    void onSuccess(String s);

    void onError(String s);
}
