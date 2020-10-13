### 如果你看到这个仓库，非常荣幸，如果想要用于您的项目中，建议先看源码，因为这是我用来做外包用来快速开发的库，里面很多内容适合我的项目但不一定适合您的项目，当然，如果需要，您可以clone源码中的部分代码用于您的项目中，如有雷同，不甚荣幸

## 目录

* [注意事项](#注意事项)
* [导入指南](#导入指南)
* [目录介绍](#目录介绍)
* [相关文档](#相关文档)
* [混淆](#混淆)
* [其他](#其他)
* [基础功能](#基础功能)
* [Demo下载](#Demo下载)
* [License](#license)

# 注意事项

> **使用前请查看注意事项**，3.x及以后版本仅支持AndroidX，可切换分支查看早期代码

> 最低支持api21

    minSdkVersion 21
    targetSdkVersion 29
    
> 开发环境

    AndroidStudio 4.1.+
    Gradle 6.5

## 必读事项

1. `quicklib`依赖`QMUI`，需要在主项目中配置`QMUI`的`styles`，参考`app`中的`theme.xml`，主题请继承`QuickTheme`
2. 需要注意quicklib中的QuickInit类，需要的话请在Application中初始化(一般用不到)。
3. 请在module的`build.gradle#android`中添加如下代码：

    ``` groovy
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ```
   
4. `BaseActivity`集成了`QMUITopBarLayout`，默认集成沉浸式状态栏(状态栏背景与TopBar背景相同)，如需使用直接使用`mTopBar`调用相应方法即可，如果不需要使用请手动调用`mTopBar.setVisibility(View.GONE);`隐藏。
5. 状态栏背景可能会与状态栏字体图标冲突，如有冲突请手动修改状态栏字体图标背景色，可参考`app`下面的`SampleBaseActivity`

    ``` java
    // 设置状态栏黑色字体图标
    QMUIStatusBarHelper.setStatusBarLightMode(mContext);
    // 设置状态栏白色字体图标
    QMUIStatusBarHelper.setStatusBarDarkMode(mContext);
    ```

6. `BaseActivity`集成了侧划关闭组件，如需关闭某个页面请在对应`Activity`覆写`protected boolean canDragBack()`

    ``` java
    @Override
    protected boolean canDragBack() {
        return false;
    }
    ```

7. 需要在`Application`中添加如下代码：

    ``` java
    QMUISwipeBackActivityManager.init(this);
    ARouter.init(this);
    Utils.init(this);
    ```
   
8. Base中封装了`viewBinding`/`dataBinding`相关代码，需在项目的`build.gradle`中启用`viewBinding`/`dataBinding`，两者可选其一，也可一起混合使用（非`dataBinding`页面使用`viewBinding`代替`findViewById`或`Butterknife`），请参考[Sample](/app)中的相关代码，请不要使用`setContentView`添加布局，应通过实现Base中的`getViewBinding`方法添加布局。

    ``` groovy
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    ```

## 关于支付模块支付宝支付的特殊说明

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

2. 请将[支付宝的aar文件](/libs)放入您项目根目录的`libs`目录中，然后在启动模块（ex：app Module）添加支付宝aar依赖

    ``` gradle
    dependencies {
        // ...
        api (name: 'alipaySdk-15.7.7-20200702160044', ext: 'aar')
    }
    ```

## 如何使用Mvc、Mvp、Mvvm模式

1. 如果使用Mvc模式，直接继承BaseActivity/BaseFragment即可
2. 如果使用Mvp模式，请参考[Demo中的Mvp实现](/app/src/main/java/com/sdwfqin/quickseed/ui/mvp)
3. 如果使用Mvvm模式，请参考[Demo中的Mvvm实现](/app/src/main/java/com/sdwfqin/quickseed/ui/mvvm)
4. 网络部分可以参考DemoApp下面的[mvpretrofit](/app/src/main/java/com/sdwfqin/quickseed/mvpretrofit)

# 导入指南

``` groovy
def quicklib = "4.5.0"

// quicklib(Base)
implementation "com.sdwfqin.quicklib:quicklib:$quicklib"
annotationProcessor "com.qmuiteam:arch-compiler:2.0.0-alpha10"
annotationProcessor "com.alibaba:arouter-compiler:1.2.2"
// 支付模块
implementation "com.sdwfqin.quicklib:paylib:$quicklib"
// Android 图片加载库（Glide封装）
implementation "com.sdwfqin.quicklib:imageloader:$quicklib"
// Android 自定义View组件
implementation "com.sdwfqin.quicklib:widget:$quicklib"
```

# 目录介绍

| 目录文件 | 解释 |
| :-------- | :--------|
| app | Demo(Java版) |
| app-kt | MVVM+Hilt+Retrofit+协程 |
| sampleCommonLibrary | Demo公共组件 |
| ~~docs~~ | ~~文档~~ |
| imageloader | 图片加载库（基于Glide） |
| libs | 公共jar/aar包 |
| paylib | 支付组件库 |
| quicklib | 基础组件库 |
| widget | View组件库 |
| config.gradle | 依赖配置 |

# 相关文档

[1.x文档](/docs/README_1_x.md)

[Wiki](/wiki)

[更新文档](/docs/update.md)

# 混淆

4.2.0开始支持自动传递混淆配置，如有问题请提Issues或单独添加需要添加的策略～

# 其他

热更新（Tinker）、Retrofit封装可参考[Sample](/app)

# 基础功能

1. quicklib

| 文件名称 | 功能 |
| :-------- | :--------|
| BaseActivity |  |
| BaseFragment |  |
| BaseMvpActivity | 支持MVP的基类Activity |
| BaseMvpFragment | 支持MVP的基类Fragment |
| RxPresenter | Presenter层封装 |
| BaseMvvmActivity | 支持MVVM的基类Activity |
| BaseMvvmFragment | 支持MVVM的基类Fragment |
| BaseViewModel | ViewModel基类 |
| WechatShareTools | 微信分享工具类 |
| ImagePreviewActivity | 图片预览Activity（多图/单图） |
| QuickBaseWebViewActivity | ViewActivity基类 |
| QuickWebViewActivity | 传入url即可 |
| QuickWebViewLoadDataActivity | 针对非url链接的网页 |
| GsonUtil | Gson工具类 |
| RxSchedulersUtils | compose()统一线程处理 |
| RxTimerUtil | RxJava定时任务 |
| RxJavaLifecycleManager | RxJava生命周期管理 |
| EventBusUtil | EventBus工具类，使用时需要配合Base基类 |
| QuickSimpleHintDialog | 可配置提示弹窗 |
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
| NoScrollViewPager | 可以禁止左右滑动的ViewPager，ViewPager2现已支持禁止滑动 |
| TrembleButton | 可以漂浮颤抖的按钮 |
| WrapContentHeightViewPager | 处理NestedScrollView嵌套Viewpager+RecyclerView |
| AutoPollRecyclerView | 跑马灯样式的RecyclerView（自动滚动） |
| AmountView | 购物车商品数量选择 |
| ~~AutoLinesLayoutManager~~ | 自动换行的布局管理器（流式布局），建议使用[flexbox-layout](https://github.com/google/flexbox-layout)代替 |
| ControlViewPager | 可动态禁止（允许）左滑/右滑的ViewPager |
| StatusPlaceholderView | 沉浸式状态栏占位 |
| WindowFloatView | 悬浮窗基类 |
| CircleProgressView | 加载进度View |

4. imageloader

| 文件名称 | 功能 |
| :-------- | :--------|
| ImageLoader | 图片加载 |

# Demo下载

https://www.pgyer.com/AndroidQuick
安装密码：111111

# License

   	Copyright 2018 zhangqin

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.