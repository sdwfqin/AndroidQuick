package io.github.sdwfqin.samplecommonlibrary.base;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.tencent.bugly.Bugly;

import io.github.sdwfqin.quicklib.BuildConfig;
import io.github.sdwfqin.quicklib.QuickInit;
import io.github.sdwfqin.samplecommonlibrary.R;

/**
 * 描述：tinker热更新配置
 *
 * @author 张钦
 * @date 2018/8/23
 */
public class SampleApplication extends Application {

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
//        Beta.canShowUpgradeActs.add(MainActivity.class);
        Bugly.init(this, "534e5a3930", BuildConfig.DEBUG);
        QuickInit.setBaseUrl(Constants.BASE_URL);
        QuickInit.setRealPath(Constants.SAVE_REAL_PATH);

        initArouter();
    }

    private void initArouter() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)¬
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this);
    }

    private void initUtils() {
        Utils.init(this);

        // 设置日志
        LogUtils.Config config = LogUtils.getConfig();
        config.setLogSwitch(true)
                .setConsoleSwitch(true);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 上拉加载，下拉刷新
     * static 代码段可以防止内存泄露
     */
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //全局设置主题颜色
            layout.setPrimaryColorsId(R.color.color_background_base, R.color.color_text_regular);
            //指定为经典Header，默认是 贝塞尔雷达Header
            return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
                    layout.setPrimaryColorsId(R.color.color_background_base, R.color.color_text_regular);
                    //指定为经典Footer，默认是 BallPulseFooter
                    return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
                }
        );
    }
}
