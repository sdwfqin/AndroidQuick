package io.github.sdwfqin.quickseed.ui.mvvm

import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import io.github.sdwfqin.quickseed.constants.ArouterConstants
import io.github.sdwfqin.quickseed.databinding.ActivityWeatherMvvmBinding
import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseMvvmActivity

/**
 * 获取天气信息 mvvm
 *
 * @author 张钦
 * @date 2020/4/14
 */
@Route(path = ArouterConstants.COMPONENTS_MVVM)
@AndroidEntryPoint
class WeatherMvvmActivity : SampleBaseMvvmActivity<ActivityWeatherMvvmBinding, WeatherViewModel>() {
    override fun getViewBinding(): ActivityWeatherMvvmBinding {
        return ActivityWeatherMvvmBinding.inflate(layoutInflater)
    }

    override fun getViewModel(): WeatherViewModel {
        return ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun initEventAndData() {
        mNavBar.setTitle("MVVM DEMO")
        mNavBar.addLeftBackImageButton().setOnClickListener { finish() }
        mBinding.viewModel = mVm
        mBinding.lifecycleOwner = this
        mVm.loadWeather()
    }

    override fun commonNetworkErrorListener(throwable: Throwable) {
        super.commonNetworkErrorListener(throwable)
        throwable.message?.let { showMsg(it) }
    }
}