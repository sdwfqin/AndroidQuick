package io.github.sdwfqin.quicklib.mvvm

import androidx.viewbinding.ViewBinding
import io.github.sdwfqin.quicklib.base.BaseFragment

/**
 * 描述：Mvvm Fragment基类
 *
 * @author 张钦
 */
abstract class BaseMvvmFragment<V : ViewBinding, VM : BaseViewModel> : BaseFragment<V>() {

    protected lateinit var mVm: VM

    override fun initViewModel() {
        mVm = getViewModel()
        mVm.isLoading.observe(viewLifecycleOwner, { isLoading: Boolean ->
            if (isLoading) {
                mBaseActivity.showProgress()
            } else {
                mBaseActivity.hideProgress()
            }
        })
        mVm.networkError.observe(
            this,
            { throwable: Throwable -> commonNetworkErrorListener(throwable) }
        )
    }

    /**
     * 获取ViewModel
     */
    protected abstract fun getViewModel(): VM

    /**
     * 通用网络异常回掉
     */
    protected fun commonNetworkErrorListener(throwable: Throwable) {
        // TODO 其实这里可以写一下默认处理方式，可以在业务模块写网络异常处理
    }
}