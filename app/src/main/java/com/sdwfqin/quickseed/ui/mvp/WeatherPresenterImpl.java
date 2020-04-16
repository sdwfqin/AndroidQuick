package com.sdwfqin.quickseed.ui.mvp;

import com.sdwfqin.quicklib.mvp.SamplePresenter;
import com.sdwfqin.quicklib.utils.rx.RxSchedulersUtils;
import com.sdwfqin.quickseed.mvpretrofit.RetrofitClient;
import com.sdwfqin.quickseed.mvpretrofit.RetrofitSubscriber;
import com.sdwfqin.quickseed.ui.mvp.contract.WeatherContract;
import com.sdwfqin.quickseed.ui.mvvm.WeatherBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Presenter
 * <p>
 *
 * @author 张钦
 * @date 2020/4/16
 */
public class WeatherPresenterImpl extends SamplePresenter<WeatherContract.WeatherView> implements WeatherContract.WeatherPresenter {

    @Override
    public void loadData() {
        Map<String, Object> map = new HashMap();
        RetrofitClient
                .getInstance()
                .gService
                .getWeather(map)
                .compose(RxSchedulersUtils.rxObservableSchedulerHelper())
                .subscribe(new RetrofitSubscriber<WeatherBean>(mView) {
                    @Override
                    protected void onSuccess(WeatherBean response) {

                    }

                    @Override
                    protected void onOtherSuccess(WeatherBean response) {
                        mView.refreshView(response);
                    }
                });
    }
}
