package io.github.sdwfqin.quicklib.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
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
    val eventLoading = MutableSharedFlow<Boolean>()

    /**
     * 通用网络请求异常
     */
    val eventError = MutableSharedFlow<Throwable>()

    fun launch(
        block: suspend () -> Unit,
        error: suspend (Throwable) -> Boolean = { true },
        complete: suspend () -> Boolean = { true }
    ) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            if (error(e)) {
                eventError.emit(e)
            }
        } finally {
            if (complete()) {
                eventLoading.emit(false)
            }
        }
    }
}