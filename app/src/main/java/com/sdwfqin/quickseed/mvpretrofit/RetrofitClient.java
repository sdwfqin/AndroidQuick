package com.sdwfqin.quickseed.mvpretrofit;

import com.sdwfqin.quickseed.BuildConfig;
import com.sdwfqin.quickseed.base.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 描述：Retrofit封装
 *
 * @author 张钦
 * @date 2017/9/25
 */
public class RetrofitClient {

    public Retrofit mRetrofit;
    public ServiceApi gService;

    private RetrofitClient() {
        mRetrofit = createRetrofit();
        gService = createService(ServiceApi.class);
    }

    private static class RetrofitClientHolder {
        private static RetrofitClient instance = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return RetrofitClientHolder.instance;
    }

    /**
     * 生成接口实现类的实例
     */
    public <T> T createService(Class<T> serviceClass) {
        return mRetrofit.create(serviceClass);
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                // 设置OkHttpclient
                .client(initOkhttpClient())
                // RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // Gson
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 每次请求都会走拦截器
     * <p>
     * 只需要修改Constants.TOKEN就可以
     */
    private OkHttpClient initOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // OkHttp日志拦截器
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }
}