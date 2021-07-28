package io.github.sdwfqin.quickseed.data.repository

import io.github.sdwfqin.quickseed.data.remote.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
        private val mClient: WeatherService
) {

    suspend fun getWeather(map: Map<String, Any>) = mClient.getWeather(map)

}