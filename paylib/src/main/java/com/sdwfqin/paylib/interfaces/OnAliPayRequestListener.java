package com.sdwfqin.paylib.interfaces;

/**
 * 描述：支付回调接口
 *
 * @author 张钦
 * @date 2018/1/25
 */
public interface OnAliPayRequestListener {

    void onSuccess(String code, String msg);

    void onError(String code, String msg);
}
