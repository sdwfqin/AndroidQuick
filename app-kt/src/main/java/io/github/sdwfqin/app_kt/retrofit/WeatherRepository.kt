package io.github.sdwfqin.app_kt.retrofit

class WeatherRepository {

    private
    val client: ServiceApi by lazy { RetrofitClient.createService(ServiceApi::class.java) }

    suspend fun getWeather(map: Map<String, Any>) = client.getWeather(map)

}