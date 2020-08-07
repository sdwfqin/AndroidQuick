package com.sdwfqin.quickseed.mvpretrofit;

import android.util.Log;

import com.sdwfqin.quickseed.BuildConfig;

import java.util.concurrent.TimeUnit;

import io.github.sdwfqin.samplecommonlibrary.base.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
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
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
            builder.addInterceptor(new HttpLoggingInterceptor(message -> {
                int strLength = message.length();
                int start = 0;
                int end = 2000;
                for (int i = 0; i < 100; i++) {
                    //剩下的文本还是大于规定长度则继续重复截取并输出
                    if (strLength > end) {
                        Log.d("okhttp", message.substring(start, end));
                        start = end;
                        end = end + 2000;
                    } else {
                        Log.d("okhttp", message.substring(start, strLength));
                        break;
                    }
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY));
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