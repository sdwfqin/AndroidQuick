package com.sdwfqin.quickseed.ui.mvp;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sdwfqin.quickseed.constants.ArouterConstants;
import com.sdwfqin.quickseed.databinding.ActivityWeatherMvpBinding;
import com.sdwfqin.quickseed.ui.mvp.contract.WeatherContract;
import com.sdwfqin.quickseed.ui.mvvm.WeatherBean;

import io.github.sdwfqin.samplecommonlibrary.base.SampleBaseMvpActivity;

/**
 * mvp Demo
 * <p>
 *
 * @author 张钦
 * @date 2020/4/16
 */
@Route(path = ArouterConstants.COMPONENTS_MVP)
public class WeatherMvpActivity extends SampleBaseMvpActivity<ActivityWeatherMvpBinding, WeatherContract.WeatherPresenter> implements WeatherContract.WeatherView {

    @Override
    protected ActivityWeatherMvpBinding getViewBinding() {
        return ActivityWeatherMvpBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initEventAndData() {

        mTopBar.setTitle("Mvp Demo");
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());

        mPresenter.loadData();
    }

    @Override
    protected WeatherContract.WeatherPresenter createPresenter() {
        return new WeatherPresenterImpl(this);
    }

    @Override
    public void refreshView(WeatherBean weatherBean) {
        mBinding.test.setText(weatherBean.toString());
    }

    @Override
    protected void initClickListener() {
        mBinding.btnRefresh.setOnClickListener(v -> {
            mPresenter.loadData();
        });
    }
}
