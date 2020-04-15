package com.sdwfqin.quickseed.retrofit;

import com.sdwfqin.quickseed.ui.mvvm.WeatherBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * 描述：Api接口
 *
 * @author 张钦
 * @date 2017/9/25
 */
public interface ServiceApi {

    @GET("free/day")
    Call<WeatherBean> getWeather(@QueryMap Map<String, Object> maps);

}
