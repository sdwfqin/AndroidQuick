package com.sdwfqin.quicklib.module.wechat.pay;

/**
 * 描述：微信统一下单业务参数
 *
 * @author zhangqin
 * @date 2018/6/8
 */
public class WechatModel {

    private String out_trade_no;
    private String money;
    private String name;
    private String detail;
    private String notify_url;

    public WechatModel(String out_trade_no, String money, String name, String detail, String notify_url) {
        this.out_trade_no = out_trade_no;
        this.money = money;
        this.name = name;
        this.detail = detail;
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

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
