package io.github.sdwfqin.app_kt.retrofit

import android.util.Log
import io.github.sdwfqin.app_kt.BuildConfig
import io.github.sdwfqin.samplecommonlibrary.base.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * 描述：Retrofit封装
 *
 * @author 张钦
 * @date 2017/9/25
 */
object RetrofitClient {

    private val mRetrofit by lazy { createRetrofit() }

    /**
     * 生成接口实现类的实例
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return mRetrofit.create(serviceClass)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) // 设置OkHttpclient
                .client(initOkhttpClient()) // RxJava2
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 字符串
                .addConverterFactory(ScalarsConverterFactory.create()) // Gson
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    /**
     * 每次请求都会走拦截器
     *
     *
     * 只需要修改Constants.TOKEN就可以
     */
    private fun initOkhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            // OkHttp日志拦截器
            builder.addInterceptor(HttpLoggingInterceptor())
            builder.addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    val strLength: Int = message.length
                    var start = 0
                    var end = 2000
                    for (i in 0..99) {
                        //剩下的文本还是大于规定长度则继续重复截取并输出
                        if (strLength > end) {
                            Log.d("okhttp", message.substring(start, end))
                            start = end
                            end += 2000
                        } else {
                            Log.d("okhttp", message.substring(start, strLength))
                            break
                        }
                    }
                }

            }).setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }
}