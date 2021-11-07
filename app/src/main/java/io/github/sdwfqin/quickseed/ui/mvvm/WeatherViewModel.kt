package io.github.sdwfqin.quickseed.ui.mvvm

import androidx.lifecycle.liveData
import com.blankj.utilcode.util.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sdwfqin.quicklib.mvvm.BaseViewModel
import io.github.sdwfqin.quickseed.data.bean.WeatherBean
import io.github.sdwfqin.quickseed.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow<UiState>(UiState.Startup)
    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<UiState> = _uiState

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

        val map: Map<String, Any> = HashMap<String, Any>()

        launch({
            _uiState.value = UiState.Success(repository.getWeather(map))
        })
    }

    sealed class UiState {
        object Startup : UiState()
        data class Success(val data: WeatherBean) : UiState()
        data class Error(val exception: Throwable) : UiState()
    }
}
