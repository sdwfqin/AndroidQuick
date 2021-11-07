package io.github.sdwfqin.quicklib.mvvm

import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import io.github.sdwfqin.quicklib.base.BaseActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 描述：Mvvm Activity基类
 *
 * @author 张钦
 */
abstract class BaseMvvmActivity<V : ViewBinding, VM : BaseViewModel> : BaseActivity<V>() {

    protected lateinit var mVm: VM

    override fun initViewModel() {
        mVm = getViewModel()
        lifecycleScope.launch {
            mVm.eventLoading.collect {
                if (it) {
                    showProgress()
                } else {
                    hideProgress()
                }
            }
            mVm.eventError.collect {
                commonNetworkErrorListener(it)
            }
        }
    }

    /**
     * 获取ViewModel
     */
    protected abstract fun getViewModel(): VM

    /**
     * 通用网络异常回掉
     */
    protected open fun commonNetworkErrorListener(throwable: Throwable) {
        LogUtils.e(throwable)
        // TODO 其实这里可以写一下默认处理方式，可以在业务模块写网络异常处理
    }
}