# 支付模块

# 微信支付

1. 调用WechatPayTools下面的wechatPayApp方法

| 属性 | 作用 | 备注 |
| :--------: | :--------: | :--------: |
| wechatPayUnifyOrder | 微信统一下单 |  |
| wechatPayApp | 调起微信支付 |  |

2. 如果想要支付回掉结果请参照示例app下面的WXPayEntryActivity
3. 注意下面的部分

    ```
    <activity
        android:name=".wxapi.WXPayEntryActivity"
        android:exported="true"
        android:screenOrientation="portrait"/>
                
    // 注意WXPayEntryActivity下面的这个方法
    @Override
    public void onResp(BaseResp resp) {
        //回掉结果监听
        WechatPay.getInstance().onResp(resp.errCode);
        LogUtils.e("onPayFinish, errCode = " + resp.errCode);
        finish();
    }
    ```

# 支付宝支付

1. AliPayTools

| 属性 | 作用 | 备注 |
| :--------: | :--------: | :--------: |
| aliPay | 支付宝支付 | 重构方法包含支付宝本地签名 |