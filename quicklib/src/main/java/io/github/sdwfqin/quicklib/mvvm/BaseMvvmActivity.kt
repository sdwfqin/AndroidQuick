package io.github.sdwfqin.quicklib.mvvm

import androidx.viewbinding.ViewBinding
import io.github.sdwfqin.quicklib.base.BaseActivity

/**
 * 描述：Mvvm Activity基类
 *
 * @author 张钦
 */
abstract class BaseMvvmActivity<V : ViewBinding, VM : BaseViewModel> : BaseActivity<V>() {

    protected lateinit var mVm: VM

    override fun initViewModel() {
        mVm = getViewModel()
        mVm.isLoading.observe(this, { isLoading: Boolean ->
            if (isLoading) {
                showProgress()
            } else {
                hideProgress()
            }
        })
        mVm.networkError.observe(
            this,
            { throwable: Throwable -> commonNetworkErrorListener(throwable) })
    }

    /**
     * 获取ViewModel
     */
    protected abstract fun getViewModel(): VM

    /**
     * 通用网络异常回掉
     */
    protected open fun commonNetworkErrorListener(throwable: Throwable) {
        // TODO 其实这里可以写一下默认处理方式，可以在业务模块写网络异常处理
    }
}