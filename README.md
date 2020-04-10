### 4.0版本全面拥抱Jetpack并且不完全向前兼容

### 如果你看到这个仓库，非常荣幸，如果想要用于您的项目中，建议先看源码，因为这是我用来做外包用来快速开发的库，里面很多内容适合我的项目但不一定适合您的项目，当然，如果需要，您可以clone源码中的部分代码用于您的项目中，如有雷同，不甚荣幸

# Gradle（使用前请查看注意事项）:

    // AndroidX
    
    // quicklib(Base)
    implementation 'com.sdwfqin.quicklib:quicklib:3.3.0'
    
    // 支付模块
    implementation 'com.sdwfqin.quicklib:paylib:3.1.0'
    
    // Android 图片加载库（Glide封装）
    implementation 'com.sdwfqin.quick:imageloader:3.2.0'
    
    // Android 自定义View组件
    implementation 'com.sdwfqin.quick:widget:3.2.0'

> 最低支持api21

    minSdkVersion 21
    targetSdkVersion 29
    
# 早期版本

[1.x文档](/docs/README_1_x.md)

# 需要注意！！！

1. `quicklib`依赖`QMUI`，需要在主项目中配置`QMUI`的`styles`，可参考`app`项目中的相应代码。V3.2+使用的`QMUI`2.0或更高版本，请参考`app`中的`theme.xml`，主题请继承`QuickTheme`
2. 需要注意quicklib中的QuickInit类，需要的话请在Application中初始化(一般用不到)。
3. `quicklib`、`widget`这几个模块因为项目引入了`AndroidUtilCode`，所以需要在`Application`初始化`Utils.init(this);`
4. 请在module的`build.gradle#android`中添加如下代码：

    ``` groovy
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ```
   
5. `BaseActivity`集成了`QMUITopBarLayout`，默认集成沉浸式状态栏(状态栏背景与TopBar背景相同)，如需使用直接使用`mTopBar`调用相应方法即可，如果不需要使用请手动调用`mTopBar.setVisibility(View.GONE);`隐藏。
6. 状态栏背景可能会与状态栏字体图标冲突，如有冲突请手动修改状态栏字体图标背景色，可参考`app`下面的`SampleBaseActivity`

    ``` java
    // 设置状态栏黑色字体图标
    QMUIStatusBarHelper.setStatusBarLightMode(mContext);
    // 设置状态栏白色字体图标
    QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
    ```

7. `BaseActivity`集成了侧划关闭组件，如需关闭某个页面请在对应`Activity`覆写`protected boolean canDragBack()`

    ``` java
    @Override
    protected boolean canDragBack() {
        return false;
    }
    ```
8. 需要在`Application`中添加如下代码：

    ``` java
    QMUISwipeBackActivityManager.init(this);
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
| StatusPlaceholderView | 沉浸式状态栏占位 |
| WindowFloatView | 悬浮窗基类 |

4. imageloader

| 文件名称 | 功能 |
| :-------- | :--------|
| ImageLoader | 图片加载 |
| CircleProgressView | 加载进度View |

# Apk https://www.pgyer.com/AndroidQuick