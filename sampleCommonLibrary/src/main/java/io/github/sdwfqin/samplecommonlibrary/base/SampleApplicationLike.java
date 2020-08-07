package io.github.sdwfqin.samplecommonlibrary.base;//package com.sdwfqin.quickseed.base;
//
//import android.annotation.TargetApi;
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.support.multidex.MultiDex;
//
//import com.blankj.utilcode.util.LogUtils;
//import com.blankj.utilcode.util.Utils;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
//import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
//import com.scwang.smartrefresh.layout.header.ClassicsHeader;
//import com.sdwfqin.quicklib.BuildConfig;
//import com.sdwfqin.quicklib.QuickInit;
//import com.sdwfqin.quickseed.R;
//import com.sdwfqin.quickseed.ui.main.MainActivity;
//import com.tencent.bugly.Bugly;
//import com.tencent.bugly.beta.Beta;
//import com.tencent.tinker.loader.app.DefaultApplicationLike;
//
///**
// * 描述：Application
// *
// * @author 张钦
// * @date 2017/8/3
// */
//public class SampleApplicationLike extends DefaultApplicationLike {
//
//    public SampleApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        // 初始化工具类
//        initUtils();
//        // 只能在某个Activity显示更新弹窗
//        Beta.canShowUpgradeActs.add(MainActivity.class);
//        Bugly.init(getApplication(), "534e5a3930", !BuildConfig.DEBUG);
//        QuickInit.setBaseUrl(Constants.BASE_URL);
//        QuickInit.setRealPath(Constants.SAVE_REAL_PATH);
//    }
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//        // you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);
//
//        // 安装tinker
//        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
//        Beta.installTinker(this);
//    }
//
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
//        getApplication().registerActivityLifecycleCallbacks(callbacks);
//    }
//
//    private void initUtils() {
//        Utils.init(getApplication());
//
//        // 设置日志
//        LogUtils.Config config = LogUtils.getConfig();
//        config.setLogSwitch(true)
//                .setConsoleSwitch(true);
//    }
//
//    /**
//     * 上拉加载，下拉刷新
//     * static 代码段可以防止内存泄露
//     */
//    static {
//        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
//            //全局设置主题颜色
//            layout.setPrimaryColorsId(R.color.gray2, R.color.black1);
//            //指定为经典Header，默认是 贝塞尔雷达Header
//            return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
//        });
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
//                    layout.setPrimaryColorsId(R.color.gray2, R.color.black1);
//                    //指定为经典Footer，默认是 BallPulseFooter
//                    return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
//                }
//        );
//    }
//}
