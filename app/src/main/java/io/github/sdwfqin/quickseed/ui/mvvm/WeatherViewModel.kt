package io.github.sdwfqin.quickseed.ui.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.blankj.utilcode.util.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sdwfqin.quicklib.mvvm.BaseViewModel
import io.github.sdwfqin.quickseed.data.bean.WeatherBean
import io.github.sdwfqin.quickseed.data.repository.WeatherRepository
import java.util.*
import javax.inject.Inject

/**
 * 天气ViewModel
 *
 * @author 张钦
 * @date 2020/4/15
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : BaseViewModel() {

    val weatherBean = MutableLiveData<WeatherBean>()

    // liveData KTX 测试
    val weatherBean2 = liveData {
        val map: Map<String, Any> = HashMap<String, Any>()
        try {
            val data = repository.getWeather(map) // loadUser is a suspend function.
            emit(data)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    fun loadWeather() {

        isLoading.postValue(true)

        val map: Map<String, Any> = HashMap<String, Any>()

        launch({
            weatherBean.postValue(repository.getWeather(map))
        }, {
            LogUtils.e(it)
            networkError.postValue(it)
        }, {
            isLoading.postValue(false)
        })
    }
}