package com.sdwfqin.quicklib.module.wechat.pay;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.sdwfqin.quicklib.module.interfaces.OnRequestListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author 张钦
 * @date 2018/1/25
 */
public class WechatPayTools {

    public static void wechatPayApp(Context mContext, String app_id, String partner_id,
                                    String wx_private_key, String prepay_id, OnRequestListener onRequestListener) {
        SortedMap<String, String> params = new TreeMap<String, String>();
        params.put("appid", app_id);
        params.put("noncestr", "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        params.put("package", "Sign=WechatPay");
        params.put("partnerid", partner_id);
        params.put("prepayid", prepay_id);
        params.put("timestamp", getCurrTime());

        String sign = getSign(params, wx_private_key);

        WechatPayModel wechatPayModel = new WechatPayModel(app_id, partner_id, prepay_id, "Sign=WechatPay", params.get("noncestr"), params.get("timestamp"), sign);
        String pay_param = new Gson().toJson(wechatPayModel);
        WechatPayTools.doWXPay(mContext, app_id, pay_param, onRequestListener);
    }

    public static void wechatPayApp(Context mContext, String app_id, String noncestr,
                                    String wpackage, String partner_id, String prepay_id,
                                    String timestamp, String sign, OnRequestListener onRequestListener) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", app_id);
        params.put("noncestr", noncestr);
        params.put("package", wpackage);
        params.put("partnerid", partner_id);
        params.put("prepayid", prepay_id);
        params.put("timestamp", timestamp);

        WechatPayModel wechatPayModel = new WechatPayModel(app_id, partner_id, prepay_id, wpackage, noncestr, timestamp, sign);
        String pay_param = new Gson().toJson(wechatPayModel);
        WechatPayTools.doWXPay(mContext, app_id, pay_param, onRequestListener);
    }

    /**
     * 获取签名 md5加密(微信支付必须用MD5加密)
     * 获取支付签名
     *
     * @param params
     * @return
     */
    public static String getSign(SortedMap<String, String> params, String wx_private_key) {
        String sign = null;
        StringBuffer sb = new StringBuffer();
        Set es = params.entrySet();
        Iterator iterator = es.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wx_private_key);
        sign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return sign;
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    public static void doWXPay(Context mContext, String wx_appid, String pay_param, final OnRequestListener onRxHttpString) {
        WechatPay.init(mContext, wx_appid);//要在支付前调用
        WechatPay.getInstance().doPay(pay_param, new WechatPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.e("微信支付成功");
                onRxHttpString.onSuccess("微信支付成功");
            }

            @Override
            public void onError(int error_code) {
                LogUtils.e(error_code);
                switch (error_code) {
                    case WechatPay.NO_OR_LOW_WX:
                        LogUtils.e("未安装微信或微信版本过低");
                        onRxHttpString.onError("未安装微信或微信版本过低");
                        break;

                    case WechatPay.ERROR_PAY_PARAM:
                        LogUtils.e("参数错误");
                        onRxHttpString.onError("参数错误");
                        break;

                    case WechatPay.ERROR_PAY:
                        LogUtils.e("支付失败");
                        onRxHttpString.onError("支付失败");
                        break;
                }
            }

            @Override
            public void onCancel() {
                LogUtils.e("支付取消");
                onRxHttpString.onError("支付取消");
            }
        });
    }
}
