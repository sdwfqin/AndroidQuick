package com.sdwfqin.quickseed.ui.mvvm;

import androidx.lifecycle.MutableLiveData;

import com.sdwfqin.quicklib.mvvm.BaseViewModel;
import com.sdwfqin.quickseed.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 天气ViewModel
 * <p>
 * 注意：ViewModel 绝不能引用视图、Lifecycle 或可能存储对 Activity 上下文的引用的任何类。
 *
 * @author 张钦
 * @date 2020/4/15
 */
public class WeatherViewModel extends BaseViewModel {

    public final MutableLiveData<WeatherBean> weatherBean = new MutableLiveData<>();

    public void loadWeather() {
        isLoading.postValue(true);
        Map<String, Object> map = new HashMap();
        RetrofitClient
                .getInstance()
                .gService.getWeather(map)
                .enqueue(new Callback<WeatherBean>() {
                    @Override
                    public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
                        weatherBean.postValue(response.body());
                        isLoading.postValue(false);
                    }

                    @Override
                    public void onFailure(Call<WeatherBean> call, Throwable t) {
                        isLoading.postValue(false);
                        networkError.postValue(t);
                    }
                });
    }
}
