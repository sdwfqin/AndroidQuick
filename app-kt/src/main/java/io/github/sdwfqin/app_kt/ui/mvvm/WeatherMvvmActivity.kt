package io.github.sdwfqin.app_kt.ui.mvvm

import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import io.github.sdwfqin.app_kt.constants.ArouterConstants
import io.github.sdwfqin.app_kt.databinding.ActivityWeatherMvvmBinding
import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseMvvmActivity

/**
 * 获取天气信息 mvvm
 *
 * @author 张钦
 * @date 2020/4/14
 */
@Route(path = ArouterConstants.COMPONENTS_MVVM)
class WeatherMvvmActivity : SampleBaseMvvmActivity<ActivityWeatherMvvmBinding, WeatherViewModel>() {
    override fun getViewBinding(): ActivityWeatherMvvmBinding {
        return ActivityWeatherMvvmBinding.inflate(layoutInflater)
    }

    override fun getViewModel(): WeatherViewModel {
        return ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun initEventAndData() {
        mTopBar.setTitle("MVVM DEMO")
        mTopBar.addLeftBackImageButton().setOnClickListener { finish() }
        mBinding.viewModel = mVm
        mBinding.lifecycleOwner = this
        mVm.loadWeather()
    }

    override fun commonNetworkErrorListener(throwable: Throwable) {
        showMsg(throwable.message)
    }
}