# 包名按照功能划分

# 配置参数放在base/Constants.java中

1. 所有的Activity活动请求吗放在这里面，例如：RESULT_CODE_1

# 日志打印使用LogUtils类，方便统一管理，例如：LogUtils.i("1234");

# 请求错误调用BaseActivity/BaseFragment中的httpError方法，可以传入错误码，方便进行统一的错误管理

# Fragment中建议使用lazyLoad()方法加载数据


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

