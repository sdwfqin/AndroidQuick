package com.sdwfqin.quicklib.mvp;

import androidx.viewbinding.ViewBinding;

import com.sdwfqin.quicklib.base.BaseFragment;

/**
 * 描述：Fragment基类
 *
 * @author 张钦
 * @date 2017/8/3
 */
public abstract class BaseMvpFragment<V extends ViewBinding, T extends BasePresenter> extends BaseFragment<V> {

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