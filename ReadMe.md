### 如果你看到这个仓库，非常荣幸，如果想要用于您的项目中，请不要直接引入，请先看源码，因为这是我用来做外包用来快速开发的库，里面很多内容适合我的项目但不一定适合您的项目，当然，如果需要，您可以clone源码中的部分代码用于您的项目中，如有雷同，不甚荣幸

    implementation 'com.android.support:appcompat-v7:27.1.0'
    implementation 'com.android.support:support-v4:27.1.0'
    implementation 'com.android.support:design:27.1.0'
    implementation 'com.android.support:cardview-v7:27.1.0'
    implementation 'com.android.support:recyclerview-v7:27.1.0'

    implementation 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    implementation 'com.github.bumptech.glide:glide:4.6.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.6.1'
    
    implementation 'com.sdwfqin.quicklib:quicklib:1.1.7'

> 最低支持api16，编译版本27，gradle4.1

    minSdkVersion 16
    targetSdkVersion 27
    
# 注意事项

1. 当前项目依赖Qmui 1.0.7，即在主项目中配置了Qmui，quicklib中的样式无需再次配置就与主项目中的样式一致，后期可能会脱离出来
2. 需要注意quicklib中的QuickInit类，建议在Application中初始化
3. 因为项目引入了AndroidUtilCode，所以需要在Application初始化（Utils.init(this);）

# 支持Mvp与Mvc模式

1. 如果使用Mvc模式，直接继承BaseActivity/BaseFragment即可
2. 如果使用Mvp模式，需要继承MvpActivity/MvpFragment，并且Contract接口或Presenter/View接口需要继承BaseView与BasePresenter<T extends BaseView>，Presenter实现类可以直接实现Presenter接口也可以继承RxPresenter<T extends BaseView>类并实现Presenter接口，他们的区别是RxPresenter里面实现了BasePresenter的接口处理了View绑定并且添加了对RxJava事件的处理

# 功能列表
```
|- quicklib
    |- BaseActivity
    |- BaseFragment
    |- MvpActivity
    |- MvpFragment
    |- RxPresenter Presenter层封装
    |- AliPayTools 支付宝支付工具类
    |- WechatPay 微信支付工具类
    |- WechatShareTools 微信分享工具类
    |- QrBarScanActivity 解析二维码Activity
    |- QrCreateCode 创建二维码工具类
    |- SeeImageActivity 图片预览Activity（多图/单图）
    |- WebViewActivity 传入url即可
    |- WebViewLoadDataActivity 针对非url链接的网页
    |- AppManager Activity栈管理
    |- GsonUtil Gson工具类
    |- ImageLoader Glide 4.x 工具类（封装了“wo”常用的）
    |- RxUtil compose()统一线程处理
    |- EventBusUtil EventBus工具类，使用时需要注意BaseActivity/BaseFragment中的方法，在里面做了封装
    |- HintDialog 可配置提示弹窗
    |- BottomDialogPhotoFragment 一个简单的可配置底部弹窗
    |- AmountView 购物车商品数量选择
    |- AutoLinesLayoutManager 自动换行的布局管理器
    |- ClickViewPager 可以点击的ViewPager
    |- DecimalEditText Double类型的EditText，支持限定小数点后的位数
    |- NoScrollViewPager 可以禁止滑动的ViewPager
    |- TrembleButton 可以漂浮颤抖的按钮
    |- WrapContentHeightViewPager 处理NestedScrollView嵌套Viewpager+RecyclerView
|- app
    |- IntroActivity 新用户引导页Activity
    |- RetrofitClient Retrofit封装（内部类单例）
    |- NetworkError 网络异常处理（在这里调用RetrofitException）
    |- RetrofitException Retrofit异常处理
    |- BaseResponse Retrofit的Response基类
```

# Apk http://fir.im/x97v

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

