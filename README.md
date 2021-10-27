### 有缘相见，不胜荣幸。v5版本计划全面拥抱Kotlin与JetPack，会移除一些早期代码，计划可见Projects。因为很多原因，当前库目前只作为业余项目维护。

# 注意事项

> **使用前请查看注意事项**，3.x及以后版本仅支持AndroidX，可切换分支查看早期代码

> 最低支持api21

    minSdkVersion 21
    targetSdkVersion 31
    
> 开发环境

    AndroidStudio 2020.3.1
    Gradle 7.0.2

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
   
4. `BaseActivity`集成了`QMUITopBarLayout`，默认集成沉浸式状态栏(状态栏背景与TopBar背景相同)，如需使用直接使用`mNavBar`调用相应方法即可，如果不需要使用请手动调用`mNavBar.setVisibility(View.GONE);`隐藏。
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
        viewBinding true
        dataBinding true
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
        api (name: 'alipaysdk-15.8.03.210428205839', ext: 'aar')
    }
    ```

# 导入指南

暂时没有发版，可以下载源码使用~

# 目录介绍

| 目录文件 | 解释 |
| :-------- | :--------|
| app | Demo |
| sampleCommonLibrary | Demo公共组件 |
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