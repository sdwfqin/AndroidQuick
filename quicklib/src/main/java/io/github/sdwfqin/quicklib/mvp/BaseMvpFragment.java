package io.github.sdwfqin.quicklib.mvp;

import androidx.viewbinding.ViewBinding;

import io.github.sdwfqin.quicklib.base.BaseFragment;

/**
 * 描述：Fragment基类
 *
 * @author 张钦
 * @date 2017/8/3
 */
public abstract class BaseMvpFragment<V extends ViewBinding, T extends IBasePresenter> extends BaseFragment<V> {

    protected T mPresenter;

    @Override
    protected void initPresenter() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    protected void removePresenter() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    protected abstract T createPresenter();
}