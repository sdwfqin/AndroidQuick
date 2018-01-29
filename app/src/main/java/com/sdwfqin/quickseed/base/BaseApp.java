package com.sdwfqin.quickseed.base;

import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.sdwfqin.quicklib.QuickInit;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.ui.MainActivity;

/**
 * 描述：Application
 *
 * @author 张钦
 * @date 2017/8/3
 */
public class BaseApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化工具类
        initUtils();
        // 只能在某个Activity显示更新弹窗
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Bugly.init(getApplicationContext(), "66666666", false);
        QuickInit.setBaseUrl(Constants.BASE_URL);
        QuickInit.setRealPath(Constants.SAVE_REAL_PATH);
        QuickInit.setLog(Constants.LOG_TYPE);
    }

    private void initUtils() {
        Utils.init(this);

        // 设置日志
        LogUtils.Config config = LogUtils.getConfig();
        config.setLogSwitch(Constants.LOG_TYPE)
                .setConsoleSwitch(Constants.LOG_TYPE);
    }

    /**
     * 上拉加载，下拉刷新
     * static 代码段可以防止内存泄露
     */
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
            //全局设置主题颜色
            layout.setPrimaryColorsId(R.color.gray2, R.color.black1);
            //指定为经典Header，默认是 贝塞尔雷达Header
            return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> {
                    layout.setPrimaryColorsId(R.color.gray2, R.color.black1);
                    //指定为经典Footer，默认是 BallPulseFooter
                    return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
                }
        );
    }
}
