package com.sdwfqin.paylib.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.sdwfqin.paylib.interfaces.OnRequestListener;

import java.util.Map;

/**
 * 描述：支付宝支付工具类
 *
 * @author 张钦
 * @date 2018/1/25
 */
public class AliPayTools {

    private static final int SDK_PAY_FLAG = 1;

    private static OnRequestListener sOnRequestListener;
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    // 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    // 同步返回需要验证的信息
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    String memo = payResult.getMemo();
                    // 判断resultStatus 为9000则代表支付成功
                    int status = 0;
                    try {
                        status = Integer.parseInt(resultStatus);
                    } catch (NumberFormatException e) {
                        status = -1;
                    }
                    sOnRequestListener.onCallback(status, memo);
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 支付
     *
     * @param activity
     * @param appid              支付宝分配给开发者的应用ID
     * @param isRsa2             签名类型
     * @param alipay_rsa_private 签名私钥
     * @param aliPayModel
     * @param onRequestListener  回调监听
     */
    public static void aliPay(final Activity activity, String appid, boolean isRsa2, String alipay_rsa_private, AliPayModel aliPayModel, OnRequestListener onRequestListener) {
        sOnRequestListener = onRequestListener;
        Map<String, String> params = AliPayOrderInfoUtil.buildOrderParamMap(appid, isRsa2, aliPayModel);
        String orderParam = AliPayOrderInfoUtil.buildOrderParam(params);

        String privateKey = alipay_rsa_private;

        String sign = AliPayOrderInfoUtil.getSign(params, privateKey, isRsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(activity);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Log.i("msp", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付 服务端返回orderInfo
     *
     * @param activity
     * @param orderInfo
     * @param onRequestListener
     */
    public static void aliPay(final Activity activity, String orderInfo, OnRequestListener onRequestListener) {
        sOnRequestListener = onRequestListener;
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(activity);
            Map<String, String> result = alipay.payV2(orderInfo, true);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 销毁
     */
    public static void detach() {
        sOnRequestListener = null;
    }
}
