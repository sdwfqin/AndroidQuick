package com.sdwfqin.quickseed.retrofit;

import com.sdwfqin.quicklib.base.QuickConstants;
import com.sdwfqin.quickseed.base.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 描述：Retrofit封装
 *
 * @author 张钦
 * @date 2017/9/25
 */
public class RetrofitClient {

    public Retrofit mRetrofit;
    public TestApi gService;

    private RetrofitClient() {
        mRetrofit = createRetrofit();
        gService = createService(TestApi.class);
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
                .baseUrl(QuickConstants.BASE_URL)
                // 设置OkHttpclient
                .client(initOkhttpClient())
                // RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 字符串
                .addConverterFactory(ScalarsConverterFactory.create())
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

        if (Constants.LOG_TYPE) {
            // OkHttp日志拦截器
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
//        builder.addInterceptor(chain -> {
//            Request original = chain.request();
//            Request request = original.newBuilder()
//                    // 设置请求头，从Debug中看到修改Constants.TOKEN请求header头也会修改
//                    .header("XX-Token", SPUtils.getInstance().getString("token", ""))
//                    .header("XX-Device-Type", "android")
//                    .method(original.method(), original.body())
//                    .build();
//            return chain.proceed(request);
//        });
        return builder.build();
    }
}