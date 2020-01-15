package com.sdwfqin.quickseed.base;

import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.sdwfqin.quicklib.BuildConfig;
import com.sdwfqin.quicklib.QuickInit;
import com.sdwfqin.quickseed.R;
import com.sdwfqin.quickseed.ui.main.MainActivity;
import com.sdwfqin.quickseed.utils.skin.QMUISkinCustManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

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

        QMUISwipeBackActivityManager.init(this);

        // 初始化工具类
        initUtils();
        // 只能在某个Activity显示更新弹窗
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Bugly.init(this, "534e5a3930", !BuildConfig.DEBUG);
        QuickInit.setBaseUrl(Constants.BASE_URL);
        QuickInit.setRealPath(Constants.SAVE_REAL_PATH);

        QMUISkinCustManager.install(this);

        QMUISkinManager skinManager = QMUISkinManager.defaultInstance(this);
        skinManager.addSkin(1, R.style.app_skin_blue);
        skinManager.addSkin(2, R.style.app_skin_dark);
        skinManager.changeSkin(QMUISkinCustManager.getCurrentSkin());

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
        if((newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){
            QMUISkinCustManager.changeSkin(QMUISkinCustManager.SKIN_DARK);
        }else if(QMUISkinCustManager.getCurrentSkin() == QMUISkinCustManager.SKIN_DARK){
            QMUISkinCustManager.changeSkin(QMUISkinCustManager.SKIN_BLUE);
        }
    }

    /**
     * 上拉加载，下拉刷新
     * static 代码段可以防止内存泄露
     */
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //全局设置主题颜色
            layout.setPrimaryColorsId(R.color.frame_gray_background_color, R.color.text_gray_dark_color);
            //指定为经典Header，默认是 贝塞尔雷达Header
            return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
                    layout.setPrimaryColorsId(R.color.frame_gray_background_color, R.color.text_gray_dark_color);
                    //指定为经典Footer，默认是 BallPulseFooter
                    return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
                }
        );
    }
}
