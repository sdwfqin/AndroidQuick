package io.github.sdwfqin.samplecommonlibrary.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.tencent.bugly.Bugly
import io.github.sdwfqin.quicklib.QuickInit
import io.github.sdwfqin.samplecommonlibrary.BuildConfig
import io.github.sdwfqin.samplecommonlibrary.R
import io.github.sdwfqin.samplecommonlibrary.utils.SkinManagerUtils

/**
 * 描述：tinker热更新配置
 *
 * @author 张钦
 * @date 2018/8/23
 */
open class SampleApplication : Application() {
    //    public SampleApplication() {
    //        super(ShareConstants.TINKER_ENABLE_ALL,
    //                "com.sdwfqin.quickseed.base.SampleApplicationLike",
    //                "com.tencent.tinker.loader.TinkerLoader",
    //                false);
    //    }
    override fun onCreate() {
        super.onCreate()

        // 初始化工具类
        initUtils()
        // 只能在某个Activity显示更新弹窗
//        Beta.canShowUpgradeActs.add(MainActivity.class);
        Bugly.init(this, "534e5a3930", BuildConfig.DEBUG)
        QuickInit.setBaseUrl(Constants.BASE_URL)
        QuickInit.setRealPath(Constants.SAVE_REAL_PATH)
        initArouter()

        SkinManagerUtils.initialize()
    }

    private fun initArouter() {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog()
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)¬
            ARouter.openDebug()
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(this)
    }

    private fun initUtils() {
        Utils.init(this)

        // 设置日志
        val config = LogUtils.getConfig()
        config.setLogSwitch(true)
            .setConsoleSwitch(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
            } // 夜间模式未启用，使用浅色主题
            Configuration.UI_MODE_NIGHT_YES -> {
            } // 夜间模式启用，使用深色主题
        }
    }

    companion object {
        /**
         * 上拉加载，下拉刷新
         * static 代码段可以防止内存泄露
         */
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context: Context?, layout: RefreshLayout? ->
                //全局设置主题颜色
                layout?.apply {
                    setPrimaryColorsId(
                        R.color.color_background_base,
                        R.color.color_text_regular
                    )
                }
                ClassicsHeader(context).setSpinnerStyle(
                    SpinnerStyle.Translate
                )
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context: Context?, layout: RefreshLayout? ->
                layout?.apply {
                    setPrimaryColorsId(
                        R.color.color_background_base,
                        R.color.color_text_regular
                    )
                }
                ClassicsFooter(context).setSpinnerStyle(
                    SpinnerStyle.Translate
                )
            }
        }
    }
}