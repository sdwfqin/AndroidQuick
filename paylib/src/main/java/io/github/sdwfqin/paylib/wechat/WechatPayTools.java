package io.github.sdwfqin.paylib.wechat;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import io.github.sdwfqin.paylib.interfaces.OnRequestListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author 张钦
 * @date 2018/1/25
 */
public class WechatPayTools {

    /**
     * 微信统一下单接口
     */
    public static final String WX_TOTAL_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 商户发起生成预付单请求
     *
     * @param mContext
     * @param appid             微信开放平台审核通过的应用APPID
     * @param mchId             微信支付分配的商户号
     * @param wxPrivateKey      微信商户平台的API证书中的密匙key
     * @param wechatModel       微信统一下单业务参数
     * @param onRequestListener
     */
    public static void wechatPayUnifyOrder(final Context mContext, final String appid,
                                           final String mchId, final String wxPrivateKey,
                                           WechatModel wechatModel, final OnRequestListener onRequestListener) {
        //随机码
        String nonce_str = getRandomStringByLength(8);
        //商品描述
        String body = wechatModel.getDetail();
        //商品订单号
        String out_trade_no = wechatModel.getOut_trade_no();
        //总金额 分
        String total_fee = wechatModel.getMoney();
        //交易起始时间(订单生成时间非必须)
        String time_start = getCurrTime();
        //App支付
        String trade_type = "APP";
        //"http://" + "域名" + "/" + "项目名" + "回调地址.do";//回调函数
        String notify_url = wechatModel.getNotify_url();
        SortedMap<String, String> params = new TreeMap<String, String>();
        params.put("appid", appid);
        //商品描述
        params.put("body", body);
        params.put("mch_id", mchId);
        params.put("nonce_str", nonce_str);
        params.put("notify_url", notify_url);
        params.put("out_trade_no", out_trade_no);
        params.put("spbill_create_ip", "8.8.8.8");
        params.put("total_fee", total_fee);
        params.put("trade_type", trade_type);
        params.put("time_start", time_start);
        String sign = "";
        sign = getSign(params, wxPrivateKey);
        //参数xml化
        String xmlParams = parseString2Xml(params, sign);

        // 使用okhttp调用统一下单接口
        MediaType xml = MediaType.parse("application/xml; charset=utf-8");
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(xml, xmlParams);
        Request request = new Request.Builder()
                .url(WX_TOTAL_ORDER)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onRequestListener.onCallback(WechatPay.WX_NETWORK_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String data = response.body().string();
                    response.close();
                    Map<String, String> mapXml = null;
                    try {
                        mapXml = getMapFromXML(data);
                    } catch (ParserConfigurationException | IOException | SAXException e) {
                        e.printStackTrace();
                    }
                    String time = getCurrTime();

                    SortedMap<String, String> sortedMap = new TreeMap<String, String>();
                    sortedMap.put("appid", appid);
                    sortedMap.put("noncestr", nonce_str);
                    sortedMap.put("package", "Sign=WechatPay");
                    sortedMap.put("partnerid", mchId);
                    sortedMap.put("prepayid", mapXml.get("prepay_id"));
                    sortedMap.put("timestamp", time);

                    wechatPayApp(mContext, appid, mchId, wxPrivateKey, sortedMap, onRequestListener);
                } catch (IOException e) {
                    onRequestListener.onCallback(WechatPay.WX_NETWORK_ERROR, e.getMessage());
                }
            }
        });
    }

    /**
     * 调起支付接口
     *
     * @param mContext
     * @param appid             微信开放平台审核通过的应用APPID
     * @param mchId             微信支付分配的商户号
     * @param wxPrivateKey      微信商户平台的API证书中的密匙key
     * @param params
     * @param onRequestListener
     */
    public static void wechatPayApp(Context mContext, String appid, String mchId, String wxPrivateKey, SortedMap<String, String> params, OnRequestListener onRequestListener) {
        String sign = getSign(params, wxPrivateKey);

        WechatPayModel wechatPayModel = new WechatPayModel(appid, mchId, params.get("prepayid"), "Sign=WechatPay", params.get("noncestr"), params.get("timestamp"), sign);
        String payParam = new Gson().toJson(wechatPayModel);
        WechatPayTools.doWXPay(mContext, appid, payParam, onRequestListener);
    }

    /**
     * 调起支付接口
     *
     * @param mContext
     * @param appId             微信开放平台审核通过的应用APPID
     * @param partnerId         微信支付分配的商户号
     * @param noncestr          随机字符串，不长于32位。
     * @param wxPrivateKey      微信商户平台的API证书中的密匙key
     * @param prepayId          微信返回的支付交易会话ID
     * @param onRequestListener
     */
    public static void wechatPayApp(Context mContext, String appId, String partnerId, String noncestr,
                                    String wxPrivateKey, String prepayId, OnRequestListener onRequestListener) {
        SortedMap<String, String> params = new TreeMap<String, String>();
        params.put("appid", appId);
        params.put("noncestr", noncestr);
        params.put("package", "Sign=WechatPay");
        params.put("partnerid", partnerId);
        params.put("prepayid", prepayId);
        params.put("timestamp", getCurrTime());

        String sign = getSign(params, wxPrivateKey);

        WechatPayModel wechatPayModel = new WechatPayModel(appId, partnerId, prepayId, "Sign=WechatPay", params.get("noncestr"), params.get("timestamp"), sign);
        String payParam = new Gson().toJson(wechatPayModel);
        WechatPayTools.doWXPay(mContext, appId, payParam, onRequestListener);
    }

    /**
     * 调起支付接口
     *
     * @param mContext
     * @param appId             微信开放平台审核通过的应用APPID
     * @param noncestr          随机字符串，不长于32位。
     * @param partnerId         微信支付分配的商户号
     * @param prepayId          微信返回的支付交易会话ID
     * @param timestamp         时间戳
     * @param sign              签名
     * @param onRequestListener
     */
    public static void wechatPayApp(Context mContext, String appId, String noncestr,
                                    String partnerId, String prepayId,
                                    String timestamp, String sign, OnRequestListener onRequestListener) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", appId);
        params.put("noncestr", noncestr);
        params.put("package", "Sign=WechatPay");
        params.put("partnerid", partnerId);
        params.put("prepayid", prepayId);
        params.put("timestamp", timestamp);

        WechatPayModel wechatPayModel = new WechatPayModel(appId, partnerId, prepayId, params.get("package"), noncestr, timestamp, sign);
        String payParam = new Gson().toJson(wechatPayModel);
        WechatPayTools.doWXPay(mContext, appId, payParam, onRequestListener);
    }

    /**
     * 调起支付
     *
     * @param mContext
     * @param wxAppid           微信开放平台审核通过的应用APPID
     * @param payParam          支付信息
     * @param onRequestListener
     */
    public static void doWXPay(Context mContext, String wxAppid, String payParam, final OnRequestListener onRequestListener) {
        // 要在支付前调用
        WechatPay.init(mContext, wxAppid);
        // 调起支付
        WechatPay.getInstance().doPay(payParam, new WechatPay.WechatPayResultCallBack() {
            @Override
            public void onSuccess() {
                onRequestListener.onCallback(WechatPay.SUCCESS_PAY, "微信支付成功");
            }

            @Override
            public void onError(int errorCode) {
                switch (errorCode) {
                    case WechatPay.NO_OR_LOW_WX:
                        onRequestListener.onCallback(WechatPay.NO_OR_LOW_WX, "未安装微信或微信版本过低");
                        break;

                    case WechatPay.ERROR_PAY_PARAM:
                        onRequestListener.onCallback(WechatPay.ERROR_PAY_PARAM, "参数错误");
                        break;

                    case WechatPay.ERROR_PAY:
                        onRequestListener.onCallback(WechatPay.ERROR_PAY, "支付失败");
                        break;
                    default:
                }
            }

            @Override
            public void onCancel() {
                onRequestListener.onCallback(WechatPay.CANCEL_PAY, "支付取消");
            }
        });
    }

    /**
     * 获取签名 md5加密(微信支付必须用MD5加密)
     * 获取支付签名
     *
     * @param params
     * @return
     */
    public static String getSign(SortedMap<String, String> params, String wxPrivateKey) {
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
        sb.append("key=" + wxPrivateKey);
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
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return outFormat.format(now);
    }

    /**
     * 参数进行XML化
     *
     * @param map,sign
     * @return
     */
    public static String parseString2Xml(Map<String, String> map, String sign) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = map.entrySet();
        Iterator iterator = es.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append("<" + k + ">" + v + "</" + k + ">");
        }
        sb.append("<sign>" + sign + "</sign>");
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 获取一定长度的随机字符串
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * xml解析
     *
     * @param xmlString
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Map<String, String> getMapFromXML(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xmlString.getBytes());
        Document document = builder.parse(is);
        //获取到document里面的全部结点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        Node node;
        Map<String, String> map = new HashMap<String, String>();
        int i = 0;
        while (i < allNodes.getLength()) {
            node = allNodes.item(i);
            if (node instanceof Element) {
                map.put(node.getNodeName(), node.getTextContent());
            }
            i++;
        }
        return map;
    }
}
