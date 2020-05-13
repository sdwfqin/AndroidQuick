package com.sdwfqin.quickseed.ui.mvvm;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.sdwfqin.quickseed.base.ArouterConstants;
import com.sdwfqin.quickseed.base.SampleBaseMvvmActivity;
import com.sdwfqin.quickseed.databinding.ActivityWeatherMvvmBinding;

/**
 * 获取天气信息 mvvm
 * <p>
 *
 * @author 张钦
 * @date 2020/4/14
 */
@Route(path = ArouterConstants.COMPONENTS_MVVM)
public class WeatherMvvmActivity extends SampleBaseMvvmActivity<ActivityWeatherMvvmBinding, WeatherViewModel> {

    @Override
    protected ActivityWeatherMvvmBinding getViewBinding() {
        return ActivityWeatherMvvmBinding.inflate(getLayoutInflater());
    }

    @Override
    protected WeatherViewModel getViewModel() {
        return new ViewModelProvider(this).get(WeatherViewModel.class);
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("MVVM DEMO");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        mBinding.setViewModel(mVm);
        mBinding.setHandlers(new WeatherMvvmHandlers());
        mBinding.setLifecycleOwner(this);

        mVm.birthDate.observe(this, LogUtils::e);

        mVm.loadWeather();
    }

    @Override
    protected void commonNetworkErrorListener(Throwable throwable) {
        showMsg(throwable.getMessage());
    }

}
