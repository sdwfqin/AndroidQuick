### 如果你看到这个仓库，非常荣幸，如果想要用于您的项目中，建议先看源码，因为这是我用来做外包用来快速开发的库，里面很多内容适合我的项目但不一定适合您的项目，当然，如果需要，您可以clone源码中的部分代码用于您的项目中，如有雷同，不甚荣幸

# Gradle（使用前请查看注意事项）:

    // quicklib(Base)
    implementation 'com.sdwfqin.quicklib:quicklib:2.0.0_beta2'
    // 如果使用butterknife请添加【可选】
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    
    // 支付模块
    implementation 'com.sdwfqin.quicklib:paylib:1.0.1'
    
    // Android 条码/二维码扫描库
    implementation 'com.sdwfqin.quicklib:qrscan:1.0.0'
    
    // Android 图片加载库（Glide封装）
    implementation 'com.sdwfqin.quicklib:imageLoader:1.0.1'
    
    // Android 自定义View组件
    implementation 'com.sdwfqin.quicklib:widget:1.0.2'
    

> 最低支持api18

    minSdkVersion 18
    targetSdkVersion 27
    
# 早期版本

[1.x文档](/docs/README_1_x.md)
    
# 注意事项

1. `quicklib`依赖`QMUI`，需要在主项目中配置`QMUI`的styles。
2. 需要注意quicklib中的QuickInit类，需要的话请在Application中初始化
3. `quicklib`、`qrscan`、`widget`这几个模块因为项目引入了AndroidUtilCode，所以需要在Application初始化（Utils.init(this);）

# 支持Mvp与Mvc模式

1. 如果使用Mvc模式，直接继承BaseActivity/BaseFragment即可
2. 如果使用Mvp模式，需要继承MvpActivity/MvpFragment，并且Contract接口或Presenter/View接口需要继承BaseView与BasePresenter<T extends BaseView>，Presenter实现类可以直接实现Presenter接口也可以继承RxPresenter<T extends BaseView>类并实现Presenter接口，他们的区别是RxPresenter里面实现了BasePresenter的接口处理了View绑定并且添加了对RxJava事件的处理

# 使用方法

[Wiki](https://github.com/sdwfqin/AndroidQuick/wiki)

# 更新文档

[更新文档](/docs/update.md)

# 功能

1. quicklib

| 文件名称 | 功能 |
| :-------- | :--------|
| BaseActivity |  |
| BaseFragment |  |
| MvpActivity |  |
| MvpFragment |  |
| RxPresenter | Presenter层封装 |
| WechatShareTools | 微信分享工具类 |
| SeeImageActivity | 图片预览Activity（多图/单图） |
| WebViewActivity | 传入url即可 |
| WebViewLoadDataActivity | 针对非url链接的网页 |
| GsonUtil | Gson工具类 |
| RxUtil | compose()统一线程处理 |
| EventBusUtil | EventBus工具类，使用时需要配合Base基类 |
| HintDialog | 可配置提示弹窗 |
| BottomDialogPhotoFragment | 一个简单的可配置底部弹窗 |
| AppManager | Activity栈管理 |
| QuickExecutor | 线程池 |

2. paylib

| 文件名称 | 功能 |
| :-------- | :--------|
| AliPayTools | 支付宝支付工具类 |
| WechatPayTools | 微信支付工具类 |

3. widget

| 文件名称 | 功能 |
| :-------- | :--------|
| PictureUploadView | 九宫格图片上传view |
| PayPwdInputView | 自定义验证码/密码View |
| ClickViewPager | 可以点击的ViewPager |
| DecimalEditText | Double类型的EditText，支持限定小数点后的位数 |
| NoScrollViewPager | Double类型的EditText，支持限定小数点后的位数 |
| TrembleButton | 可以漂浮颤抖的按钮 |
| WrapContentHeightViewPager | 处理NestedScrollView嵌套Viewpager+RecyclerView |
| AutoPollRecyclerView | 跑马灯样式的RecyclerView（自动滚动） |
| AmountView | 购物车商品数量选择 |
| AutoLinesLayoutManager | 自动换行的布局管理器（流式布局） |

4. qrscan

| 文件名称 | 功能 |
| :-------- | :--------|
| QrBarScanActivity | 二维码/条码扫描Activity |
| QrBarTool | 二维码/条码解析工具类 |
| QrCreateTool | 创建二维码 |

5. imageloader

| 文件名称 | 功能 |
| :-------- | :--------|
| ImageLoader | 图片加载 |
| CircleProgressView | 加载进度View |

# Apk http://fir.im/x97v