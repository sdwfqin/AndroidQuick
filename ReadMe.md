### 如果你看到这个仓库，非常荣幸，如果想要用于您的项目中，请不要直接引入，请先看源码，因为这是我用来做外包用来快速开发的库，里面很多内容适合我的项目但不一定适合您的项目，当然，如果需要，您可以clone源码中的部分代码用于您的项目中，如有雷同，不甚荣幸

    implementation 'com.android.support:appcompat-v7:27.0.2'
    implementation 'com.android.support:support-v4:27.0.2'
    implementation 'com.android.support:design:27.0.2'
    implementation 'com.android.support:cardview-v7:27.0.2'
    implementation 'com.android.support:recyclerview-v7:27.0.2'

    implementation 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    implementation 'com.github.bumptech.glide:glide:4.5.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.5.0'
    
    implementation 'com.sdwfqin.quicklib:quicklib:1.0.4'

> 最低支持api16，编译版本27，gradle4.1

    minSdkVersion 16
    targetSdkVersion 27
    
# 当前项目依赖Qmui 1.0.7，后期可能会脱离出来

# 本项目中的支付与二维码模块clone自RxTools

https://github.com/vondear/RxTools

## 支付工具类
### 支付宝支付:

        AliPayTools.aliPay(mContext,
            APP_ID,//支付宝分配的APP_ID
            isRSA2,//是否是 RSA2 加密
            RSA_PRIVATE,// RSA 或 RSA2 字符串
            new AliPayModel(order_id,//订单ID (唯一)
                            money,//价格
                            name,//商品名称
                            detail),//商品描述详情 (用于显示在 支付宝 的交易记录里)
            new onRequestListener() {
                @Override
                public void onSuccess(String s) {RxToast.success("支付成功");}

                @Override
                public void onError(String s) {RxToast.error("支付失败");
            }
        });

### 微信支付:

> 第一种情景: 支付操作全部在APP端完成(包括统一下单接口) 即支付过程无后台参与

        WechatPayTools.wechatPayUnifyOrder(mContext,
            WX_APP_ID, //微信分配的APP_ID
            WX_PARTNER_ID, //微信分配的 PARTNER_ID (商户ID)
            WX_PRIVATE_KEY, //微信分配的 PRIVATE_KEY (私钥)
            new WechatModel(order_id, //订单ID (唯一)
                            money, //价格
                            name, //商品名称
                            detail), //商品描述详情
            new onRequestListener() {
                @Override
                public void onSuccess(String s) {}

                @Override
                public void onError(String s) {}
        });

> 第二种情景: 从后台获取到 prepayid（预支付订单ID） 之后,在App端进行支付操作

        wechatPayApp(mContext,
            app_id, //微信分配的APP_ID
            partner_id, //微信分配的 PARTNER_ID (商户ID)
            wx_private_key, //微信分配的 PRIVATE_KEY (私钥)
            prepay_id, //订单ID (唯一)
            new onRequestListener() {
                @Override
                public void onSuccess(String s) {}

                @Override
                public void onError(String s) {}
        });

### 微信分享:

>  分享网页

        WechatShareTools.init(mContext, WX_APP_ID);//初始化

        String url = "https://github.com/vondear/RxTools";//网页链接

        String description = "工欲善其事必先利其器！";//描述

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//获取Bitmap
        byte[] bitmapByte = RxImageTool.bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG);//将 Bitmap 转换成 byte[]

        mWechatShareModel = new WechatShareModel(url, "APP名称", description, bitmapByte);

        //Friend 分享微信好友,Zone 分享微信朋友圈,Favorites 分享微信收藏
        WechatShareTools.shareURL(mWechatShareModel, WechatShareTools.SharePlace.Friend);//分享操作

