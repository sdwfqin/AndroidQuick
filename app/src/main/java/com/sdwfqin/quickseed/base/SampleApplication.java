package com.sdwfqin.quickseed.base;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.sdwfqin.quicklib.BuildConfig;
import com.sdwfqin.quicklib.QuickInit;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.ui.MainActivity;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import androidx.multidex.MultiDexApplication;

/**
 * 描述：tinker热更新配置
 *
 * @author 张钦
 * @date 2018/8/23
 */
public class SampleApplication extends MultiDexApplication {

//    public SampleApplication() {
//        super(ShareConstants.TINKER_ENABLE_ALL,
//                "com.sdwfqin.quickseed.base.SampleApplicationLike",
//                "com.tencent.tinker.loader.TinkerLoader",
//                false);
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化工具类
        initUtils();
        // 只能在某个Activity显示更新弹窗
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Bugly.init(this, "534e5a3930", !BuildConfig.DEBUG);
        QuickInit.setBaseUrl(Constants.BASE_URL);
        QuickInit.setRealPath(Constants.SAVE_REAL_PATH);
        QMUISwipeBackActivityManager.init(this);
    }

    private void initUtils() {
        Utils.init(this);

        // 设置日志
        LogUtils.Config config = LogUtils.getConfig();
        config.setLogSwitch(true)
                .setConsoleSwitch(true);
    }

    /**
     * 上拉加载，下拉刷新
     * static 代码段可以防止内存泄露
     */
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //全局设置主题颜色
            layout.setPrimaryColorsId(R.color.gray2, R.color.black1);
            //指定为经典Header，默认是 贝塞尔雷达Header
            return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
                    layout.setPrimaryColorsId(R.color.gray2, R.color.black1);
                    //指定为经典Footer，默认是 BallPulseFooter
                    return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
                }
        );
    }
}
