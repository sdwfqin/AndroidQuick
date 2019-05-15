### 支持AndroidX，正在测试，欢迎提交issues与pr

### 不支持AndroidX请[切换分支到2.x](https://github.com/sdwfqin/AndroidQuick/tree/2.x)

### 如果你看到这个仓库，非常荣幸，如果想要用于您的项目中，建议先看源码，因为这是我用来做外包用来快速开发的库，里面很多内容适合我的项目但不一定适合您的项目，当然，如果需要，您可以clone源码中的部分代码用于您的项目中，如有雷同，不甚荣幸

# Gradle（使用前请查看注意事项）:

    // 支持AndroidX
    
    // quicklib(Base)
    implementation 'com.sdwfqin.quicklib:quicklib:3.0.0-beta5'
    // 如果使用butterknife请添加【可选】
    annotationProcessor 'com.jakewharton:butterknife-compiler:10.1.0'
    
    // 支付模块
    implementation 'com.sdwfqin.quicklib:paylib:3.0.0-beta3'
    
    // Android 图片加载库（Glide封装）
    implementation 'com.sdwfqin.quick:imageloader:3.0.0-beta1'
    
    // Android 自定义View组件
    implementation 'com.sdwfqin.quick:widget:3.0.0-beta6'
    
    ==================== AndroidX 分界线 ====================
    
    // 不支持AndroidX
    
    // quicklib(Base)
    implementation 'com.sdwfqin.quicklib:quicklib:2.3.1'
    // 如果使用butterknife请添加【可选】
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
    
    // 支付模块
    implementation 'com.sdwfqin.quicklib:paylib:1.0.5'
    
    // Android 图片加载库（Glide封装）
    implementation 'com.sdwfqin.quick:imageloader:2.0.2'
    
    // Android 自定义View组件
    implementation 'com.sdwfqin.quick:widget:1.0.7'
    

> 最低支持api18

    minSdkVersion 18
    targetSdkVersion 28
    
# 早期版本

[1.x文档](/docs/README_1_x.md)

# 需要注意！！！

1. `quicklib`依赖`QMUI`，需要在主项目中配置`QMUI`的`styles`，可参考`app`项目中的相应代码。
2. 需要注意quicklib中的QuickInit类，需要的话请在Application中初始化(一般用不到)。
3. `quicklib`、`qrscan`、`widget`这几个模块因为项目引入了`AndroidUtilCode`，所以需要在`Application`初始化`Utils.init(this);`
4. 请在module的`build.gradle#android`中添加如下代码：

    ``` groovy
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ```

# 关于支付模块支付宝支付的特殊说明

> 因支付宝SDK改用aar打包，所以使用时需要添加如下代码

1. 在您项目根目录的`build.gradle`中，添加下面的内容，将`libs`目录作为依赖仓库

    ``` gradle
    allprojects {
        repositories {
    
            // 添加下面的内容
            flatDir {
                dirs '../libs'
            }
    
            // ... jcenter() 等其他仓库
        }
    }
    ```

2. 请将[支付宝的aar文件](https://github.com/sdwfqin/AndroidQuick/tree/3.x/libs)放入您项目根目录的`libs`目录中（没有可以新建，文件名字不要变，文件夹名字跟上面的名字匹配起来就可以）

# 支持Mvp与Mvc模式

1. 如果使用Mvc模式，直接继承BaseActivity/BaseFragment即可
2. 如果使用Mvp模式，需要继承MvpActivity/MvpFragment，并且Contract接口或Presenter/View接口需要继承BaseView与BasePresenter<T extends BaseView>，Presenter实现类可以直接实现Presenter接口也可以继承SamplePresenter<T extends BaseView>类并实现Presenter接口，他们的区别是SamplePresenter里面实现了BasePresenter的接口处理了View绑定
3. 网络部分可以参考DemoApp下面的`mvpretrofit`

# 使用方法

[Wiki](https://github.com/sdwfqin/AndroidQuick/wiki)

# 更新文档

[更新文档](/docs/update.md)

# 混淆

可参考[Sample混淆文件](/app/proguard-rules.pro)

# 其他

热更新（Tinker）、Retrofit封装可参考[Sample](/app)

# 功能

1. quicklib

| 文件名称 | 功能 |
| :-------- | :--------|
| BaseActivity |  |
| BaseFragment |  |
| BaseMvpActivity |  |
| BaseMvpFragment |  |
| RxPresenter | Presenter层封装 |
| WechatShareTools | 微信分享工具类 |
| ImagePreviewActivity | 图片预览Activity（多图/单图） |
| BaseWebView | ViewActivity基类 |
| WebViewActivity | 传入url即可 |
| WebViewLoadDataActivity | 针对非url链接的网页 |
| GsonUtil | Gson工具类 |
| RxSchedulersUtils | compose()统一线程处理 |
| RxTimerUtil | RxJava定时任务 |
| EventBusUtil | EventBus工具类，使用时需要配合Base基类 |
| HintDialog | 可配置提示弹窗 |
| AppManager | Activity栈管理 |
| QuickExecutor | 线程池 |
| ImageWatermarkUtils | 图片水印工具类 |
| IClickListener | 按钮防抖 |

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
| NoScrollViewPager | 可以禁止左右滑动的ViewPager |
| TrembleButton | 可以漂浮颤抖的按钮 |
| WrapContentHeightViewPager | 处理NestedScrollView嵌套Viewpager+RecyclerView |
| AutoPollRecyclerView | 跑马灯样式的RecyclerView（自动滚动） |
| AmountView | 购物车商品数量选择 |
| ~~AutoLinesLayoutManager~~ | 自动换行的布局管理器（流式布局），建议使用[flexbox-layout](https://github.com/google/flexbox-layout)代替 |
| ControlViewPager | 可动态禁止（允许）左滑/右滑的ViewPager |

4. imageloader

| 文件名称 | 功能 |
| :-------- | :--------|
| ImageLoader | 图片加载 |
| CircleProgressView | 加载进度View |

# Apk https://www.pgyer.com/tNyb