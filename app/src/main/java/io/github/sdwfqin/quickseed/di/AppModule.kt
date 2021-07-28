package io.github.sdwfqin.quickseed.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.sdwfqin.quickseed.BuildConfig
import io.github.sdwfqin.quickseed.data.remote.WeatherService
import io.github.sdwfqin.samplecommonlibrary.base.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

/**
 * 描述：Retrofit封装
 *
 * @author 张钦
 * @date 2017/9/25
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL) // 设置OkHttpclient
                .client(okHttp) // RxJava2
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 字符串
                .addConverterFactory(ScalarsConverterFactory.create()) // Gson
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            // OkHttp日志拦截器
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