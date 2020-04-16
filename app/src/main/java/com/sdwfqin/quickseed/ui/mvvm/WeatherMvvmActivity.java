package com.sdwfqin.quickseed.ui.mvvm;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sdwfqin.quicklib.mvvm.BaseMvvmActivity;
import com.sdwfqin.quickseed.base.ArouterConstants;
import com.sdwfqin.quickseed.databinding.ActivityWeatherMvvmBinding;

/**
 * 获取天气信息 mvvm
 * <p>
 *
 * @author 张钦
 * @date 2020/4/14
 */
@Route(path = ArouterConstants.COMPONENTS_MVVM)
public class WeatherMvvmActivity extends BaseMvvmActivity<ActivityWeatherMvvmBinding, WeatherViewModel> {

    @Override
    protected ActivityWeatherMvvmBinding getViewBinding() {
        return ActivityWeatherMvvmBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<WeatherViewModel> getViewModel() {
        return WeatherViewModel.class;
    }

    @Override
    protected void initEventAndData() {
        mTopBar.setTitle("MVVM DEMO");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        mBinding.setViewModel(mVm);
        mBinding.setLifecycleOwner(this);

        mVm.loadWeather();
    }

    @Override
    protected void initClickListener() {
    }

    @Override
    protected void commonNetworkErrorListener(Throwable throwable) {
        showMsg(throwable.getMessage());
    }
}
