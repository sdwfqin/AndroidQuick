package com.sdwfqin.paylib.alipay;

/**
 * 描述：业务参数
 *
 * @author zhangqin
 * @date 2018/6/8
 */
public class AliPayModel {
    /**
     * 商户网站唯一订单号
     */
    private String out_trade_no;
    /**
     * 订单总金额，单位为元，精确到小数点后两位
     */
    private String money;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字
     */
    private String name;
    /**
     * 对一笔交易的具体描述信息。
     */
    private String detail;
    /**
     * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
     */
    private String notify_url;
    /**
     * 发送请求时间
     */
    private String timestamp;

    public AliPayModel(String out_trade_no, String money, String name, String detail, String notify_url, String timestamp) {
        this.out_trade_no = out_trade_no;
        this.money = money;
        this.name = name;
        this.detail = detail;
        this.notify_url = notify_url;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
