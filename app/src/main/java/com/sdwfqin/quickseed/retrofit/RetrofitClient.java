package com.sdwfqin.quickseed.retrofit;

import android.util.Log;

import com.sdwfqin.quickseed.BuildConfig;
import com.sdwfqin.quickseed.base.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
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
        // 通过拦截器添加除method以外的系统级参数
//        builder.addInterceptor(chain -> {
//            Request request = chain.request();
//            if (request.method().equals("POST")) {
//                if (request.body() instanceof FormBody) {
//                    FormBody.Builder bodyBuilder = new FormBody.Builder();
//                    FormBody formBody = (FormBody) request.body();
//                    bodyBuilder
//                            .addEncoded("appKey", "002")
//                            .addEncoded("v", "1.0")
//                            .addEncoded("format", "JSON")
//                            .addEncoded("type", "1");
//                    //把原来的参数添加到新的构造器
//                    for (int i = 0; i < formBody.size(); i++) {
//                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
//                    }
//                    formBody = bodyBuilder.build();
//                    request = request.newBuilder().post(formBody).build();
//                }
//            }
//            return chain.proceed(request);
//        });
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
        return builder.build();
    }
}