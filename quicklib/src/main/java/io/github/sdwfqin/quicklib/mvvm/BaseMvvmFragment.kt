package io.github.sdwfqin.quicklib.mvvm;

import androidx.viewbinding.ViewBinding;

import io.github.sdwfqin.quicklib.base.BaseFragment;

/**
 * 描述：Mvvm Fragment基类
 *
 * @author 张钦
 */
public abstract class BaseMvvmFragment<V extends ViewBinding, VM extends BaseViewModel> extends BaseFragment<V> {

    protected VM mVm;

    @Override
    protected void initViewModel() {
        mVm = getViewModel();

        mVm.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                mBaseActivity.showProgress();
            } else {
                mBaseActivity.hideProgress();
            }
        });
        mVm.networkError.observe(this, this::commonNetworkErrorListener);
    }

    /**
     * 获取ViewModel
     */
    protected abstract VM getViewModel();

    /**
     * 通用网络异常回掉
     */
    protected void commonNetworkErrorListener(Throwable throwable) {
        // TODO 其实这里可以写一下默认处理方式，可以在业务模块写网络异常处理
    }
}