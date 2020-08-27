package io.github.sdwfqin.app_kt.data.repository

import io.github.sdwfqin.app_kt.data.remote.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
        private val mClient: WeatherService
) {

    suspend fun getWeather(map: Map<String, Any>) = mClient.getWeather(map)

}