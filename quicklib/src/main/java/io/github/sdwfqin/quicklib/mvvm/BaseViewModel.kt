package io.github.sdwfqin.quicklib.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * BaseViewModel
 *
 * @author 张钦
 * @date 2020/4/15
 */
open class BaseViewModel : ViewModel() {
    /**
     * 加载窗状态
     */
    val isLoading = MutableLiveData<Boolean>()

    /**
     * 通用网络请求异常
     */
    val networkError = MutableLiveData<Throwable>()

    fun launch(
        block: suspend () -> Unit,
        error: suspend (Throwable) -> Unit,
        complete: suspend () -> Unit
    ) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        } finally {
            complete()
        }
    }
}