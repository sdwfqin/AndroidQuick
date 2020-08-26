package io.github.sdwfqin.app_kt.retrofit

import io.github.sdwfqin.app_kt.ui.mvvm.WeatherBean
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * 描述：Api接口
 *
 * @author 张钦
 * @date 2017/9/25
 */
interface ServiceApi {

    @GET("free/day")
    suspend fun getWeather(@QueryMap maps: Map<String, @JvmSuppressWildcards Any>): WeatherBean
}