package com.sdwfqin.paylib.wechat;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：微信支付
 * <p>
 * 外部调起支付请使用WechatPayTools中的相关方法
 * <p>
 * 请在WXPayEntryActivity中调用 WechatPay.getInstance().onResp(resp.errCode);
 *
 * @author 张钦
 * @date 2018/1/25
 */
public class WechatPay {

    /**
     * 未安装微信或微信版本过低
     */
    public static final int SUCCESS_PAY = 0;
    /**
     * 未安装微信或微信版本过低
     */
    public static final int NO_OR_LOW_WX = 1;
    /**
     * 支付参数错误
     */
    public static final int ERROR_PAY_PARAM = 2;
    /**
     * 支付失败
     */
    public static final int ERROR_PAY = 3;
    /**
     * 支付取消
     */
    public static final int CANCEL_PAY = 4;
    /**
     * 统一下单网络异常
     */
    public static final int WX_NETWORK_ERROR = 5;

    private static WechatPay sWechatPay;
    private IWXAPI mWXApi;
    private String mPayParam;
    private WechatPayResultCallBack mCallback;

    public WechatPay(Context context, String wxAppid) {
        mWXApi = WXAPIFactory.createWXAPI(context, null);
        mWXApi.registerApp(wxAppid);
    }

    public static void init(Context context, String wxAppid) {
        if (sWechatPay == null) {
            sWechatPay = new WechatPay(context, wxAppid);
        }
    }

    public static WechatPay getInstance() {
        return sWechatPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * 发起微信支付
     */
    public void doPay(String payParam, WechatPayResultCallBack callback) {
        mPayParam = payParam;
        mCallback = callback;

        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }

        JSONObject param = null;
        try {
            param = new JSONObject(mPayParam);
        } catch (JSONException e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }
        if (TextUtils.isEmpty(param.optString("appid")) || TextUtils.isEmpty(param.optString("partnerid"))
                || TextUtils.isEmpty(param.optString("prepayid")) || TextUtils.isEmpty(param.optString("packageValue")) ||
                TextUtils.isEmpty(param.optString("noncestr")) || TextUtils.isEmpty(param.optString("timestamp")) ||
                TextUtils.isEmpty(param.optString("sign"))) {
            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = param.optString("appid");
        req.partnerId = param.optString("partnerid");
        req.prepayId = param.optString("prepayid");
        req.packageValue = param.optString("packageValue");
        req.nonceStr = param.optString("noncestr");
        req.timeStamp = param.optString("timestamp");
        req.sign = param.optString("sign");

        mWXApi.sendReq(req);
    }

    /**
     * 支付回调响应
     *
     * @param errorCode
     */
    public void onResp(int errorCode) {
        if (mCallback == null) {
            return;
        }

        // 成功
        if (errorCode == 0) {
            mCallback.onSuccess();
            // 错误
        } else if (errorCode == -1) {
            mCallback.onError(ERROR_PAY);
            // 取消
        } else if (errorCode == -2) {
            mCallback.onCancel();
        }

        mCallback = null;
    }

    /**
     * 检测是否支持微信支付
     *
     * @return
     */
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    /**
     * 支付结果回调
     */
    public interface WechatPayResultCallBack {
        /**
         * 支付成功
         */
        void onSuccess();

        /**
         * 支付失败
         *
         * @param errorCode
         */
        void onError(int errorCode);

        /**
         * 支付取消
         */
        void onCancel();
    }

    /**
     * 销毁方法
     */
    public static void detach(){
        if (sWechatPay != null){
            sWechatPay.getWXApi().detach();
        }
        sWechatPay = null;
    }
}